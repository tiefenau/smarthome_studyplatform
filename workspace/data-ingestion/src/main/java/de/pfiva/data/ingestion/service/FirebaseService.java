package de.pfiva.data.ingestion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pfiva.data.ingestion.ConfigProperties;
import de.pfiva.data.ingestion.DataIngestionProperties;
import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.model.notification.Data;
import de.pfiva.data.model.notification.RequestModel;

@Service
public class FirebaseService {
	
	@Autowired DataIngestionProperties properties;
	@Autowired ConfigProperties configProperties;
	@Autowired ObjectMapper objectMapper;
	@Autowired RestTemplate restTemplate;
	@Autowired NLUDataIngestionDBService dbService;
	
	private static Logger logger = LoggerFactory.getLogger(FirebaseService.class);

	public void sendRequestToFirebaseServer(Data data, String clientName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String firebaseServerKey = "key=" + configProperties.getConfigValues().get("pfiva_firebase_server_key");
		headers.set(HttpHeaders.AUTHORIZATION, firebaseServerKey);
		
		RequestModel requestModel = new RequestModel();
		requestModel.setData(data);
		requestModel.setTo(getClientToken(clientName));
		
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(requestModel);
		} catch (JsonProcessingException e) {
			logger.error("Error while forming request body for firebase");
		}
		logger.info("Request body for firebase [" + requestBody + "]");
				
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestBody, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(properties.getFirebaseUrl(),
				HttpMethod.POST, requestEntity, String.class);
		logger.info("Response from firebase server [" + response.getBody() + "]");
	}
	
	private String getClientToken(String clientName) {
		return dbService.getClientToken(clientName);
	}
}
