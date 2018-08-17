package de.pfiva.data.ingestion.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pfiva.data.model.snips.SnipsOutput;

@Service
public class SnipsNLUService {

	private static final Logger logger = LoggerFactory.getLogger(SnipsNLUService.class);
	
	private static final String NLU_ENGINE_URL = "http://127.0.0.1:5000";
	
	@Autowired RestTemplate restTemplate;
	
	public SnipsOutput classifyIntents(String query) {
		ResponseEntity<String> response = null;
		if(query != null && !query.isEmpty()) {
			response = executeNLUEngine(query);
		}
		return parseToModelObject(response);
	}
	
	private ResponseEntity<String> executeNLUEngine(String query) {
		String url = NLU_ENGINE_URL + "/intents";
		logger.info("Sending request to url :" + url 
				+ ", with user query [" + query + "].");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("query", query);
		
		HttpEntity<?> requestEntity = new HttpEntity<Object>(body, headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			logger.info("Response from server\n" + response.getBody());
		} catch(RestClientException e) {
			logger.error("Error fetching data from NLU Engine at " + NLU_ENGINE_URL);
		}
		
		return response;
	}
	
	private SnipsOutput parseToModelObject(ResponseEntity<String> response) {
		ObjectMapper mapper = new ObjectMapper();
		SnipsOutput output = null;
		
		if(response == null) {
			return null;
		}
		
		try {
			output = mapper.readValue(response.getBody(), SnipsOutput.class);
		} catch (JsonParseException e) {
			logger.error("Error while parsing json to model", e);
		} catch (JsonMappingException e) {
			logger.error("Error while parsing json to model", e);
		} catch (IOException e) {
			logger.error("Error while parsing json to model", e);
		}
		return output;
	}
}
