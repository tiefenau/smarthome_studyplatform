package de.pfiva.speech.text.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.pfiva.speech.text.client.service.GoogleCloudService;

@CrossOrigin
@RestController
@RequestMapping(value = "pfiva")
public class SpeechTextClientController {

	private static Logger logger = LoggerFactory.getLogger(SpeechTextClientController.class);
	
	@Autowired private GoogleCloudService googleCloudService;
	@Autowired RestTemplate restTemplate;
	@Autowired SpeechTextClientProperties properties;
	
	@RequestMapping(value = "/speech-to-text", method = RequestMethod.POST)
	public void getspeechToText(@RequestBody String requestData) {
		Map<String, String> responseMap = null;
		try {
			responseMap = parseResponseBody(requestData);
		} catch (UnsupportedEncodingException e) {
			logger.error("Error fetching request data");
		}
		
		String fileName = responseMap.get("fileName");
		String api = responseMap.get("api");
		String language = responseMap.get("language");
		String user = responseMap.get("user");
		
		logger.info("Received new request," + requestData);
		String text = googleCloudService.getSpeechToText(fileName, language);
		logger.info("Text captured : " + text);
		if(text != null) {
			forwardRequestToDataIngestionPipeline(text, user);			
		}
	}
	
	private void forwardRequestToDataIngestionPipeline(String text, String user) {
		String url = properties.getDataIngestionPipelineUrl();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("userQuery", text);
		body.add("username", user);
		
		HttpEntity<?> requestEntity = new HttpEntity<Object>(body, headers);
		try {
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
			logger.info("Request sent to data ingestion pipeline");
		} catch(RestClientException e) {
			logger.error("Error sending request to " + properties.getDataIngestionPipelineUrl());
		}
	}

	public Map<String, String> parseResponseBody(String requestBody) throws UnsupportedEncodingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		String decode = URLDecoder.decode(requestBody, "UTF-8");
		String[] keyValuePairs = decode.split("&");
		for(String pair : keyValuePairs) {
			String[] entry = pair.split("=");
			responseMap.put(entry[0].trim(), entry[1].trim());
		}
		return responseMap;
	}
}
