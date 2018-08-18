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

import de.pfiva.data.ingestion.DataIngestionProperties;
import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.model.IntentNames;
import de.pfiva.data.ingestion.model.Tuple;
import de.pfiva.data.model.notification.Data;
import de.pfiva.data.model.notification.RequestModel;
import de.pfiva.data.model.snips.SnipsOutput;

@Service
public class FeedbackService {

	private static Logger logger = LoggerFactory.getLogger(FeedbackService.class);
	private static final String DEFAULT_FEEDBACK_QUERY = "What did you ask about?";
	
	@Autowired RestTemplate restTemplate;
	@Autowired DataIngestionProperties properties;
	@Autowired NLUDataIngestionDBService dbService;
	@Autowired ObjectMapper objectMapper;
	
	public enum FEEDBACK_TYPE {
		GENERAL, INTENT_CLASSIFIED;
	}
	
	public void sendFeedback(SnipsOutput snipsOutput, int queryId) {
		// 1. Generate Feedback
		// 2. Push feedback to db
		// 3. Send Feedback
		
		Tuple<FEEDBACK_TYPE, String> feeback = generateFeedbackQuery(snipsOutput);
		int feedbackId = pushFeedbackToDB(feeback, queryId);
		postFeedbackToFirebase(feeback, feedbackId);
	}
	
	private int pushFeedbackToDB(Tuple<FEEDBACK_TYPE, String> feeback, int queryId) {
		int feedbackId = 0;
		if(feeback != null) {
			feedbackId = dbService.pushFeedbackToDB(queryId, feeback.getY());
		}
		return feedbackId;
	}

	private Tuple<FEEDBACK_TYPE, String> generateFeedbackQuery(SnipsOutput snipsOutput) {
		// Feedback query can be generated by: 
		// 1. knowing the intent
		// 2. general feedback
		
		Tuple<FEEDBACK_TYPE, String> feedback = null;
		if(snipsOutput !=null) {
			String userQuery = snipsOutput.getInput();
			if(userQuery != null && !userQuery.trim().isEmpty()) {
				if(snipsOutput.getIntent() != null) {
					String feedbackQuery = "Did you ask about "
							+ parseIntentToUserReadableString(snipsOutput
									.getIntent().getIntentName())
							+ "?";
					feedback = new Tuple<FEEDBACK_TYPE, 
							String>(FEEDBACK_TYPE.INTENT_CLASSIFIED, feedbackQuery);
				} else {
					feedback = new Tuple<FEEDBACK_TYPE,
							String>(FEEDBACK_TYPE.GENERAL, DEFAULT_FEEDBACK_QUERY);
				}
			}
			logger.info("Feedback generated of type [" + feedback.getX() + "],"
					+ " feedback query [" + feedback.getY() + "]");
		}
		return feedback;
	}
	
	private void postFeedbackToFirebase(Tuple<FEEDBACK_TYPE, String> feeback, int feedbackId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String firebaseServerKey = "key=" + properties.getFirebaseServerKey();
		headers.set(HttpHeaders.AUTHORIZATION, firebaseServerKey);
		
		RequestModel requestModel = new RequestModel();
		Data data = new Data();
		data.setFeedbackId(feedbackId);
		data.setText(feeback.getY());
		requestModel.setData(data);
		requestModel.setTo(getClientToken());
		
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

	private String getClientToken() {
		return dbService.getClientToken(properties.getClientName());
	}

	private String parseIntentToUserReadableString(String intentName) {
		String parsedIntent = null;
		if(intentName != null && !intentName.trim().isEmpty()) {
			switch (intentName) {
			case IntentNames.SEARCHWEATHERFORECAST:
				parsedIntent = "weather";
				break;
			case IntentNames.SEARCHWEATHERFORECASTCONDITION:
				parsedIntent = "weather forecast conditions";
				break;
			case IntentNames.SEARCHWEATHERFORECASTITEM:
				parsedIntent = "weather forecast conditions";
				break;
			case IntentNames.SEARCHWEATHERFORECASTTEMPERATURE:
				parsedIntent = "weather forecast conditions";
			default:
				parsedIntent = IntentNames.UNKNOWN_INTENT;
				break;
			}
		}
		return parsedIntent;
	}

}