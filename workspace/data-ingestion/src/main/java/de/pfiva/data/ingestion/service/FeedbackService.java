package de.pfiva.data.ingestion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.ingestion.DataIngestionProperties;
import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.model.IntentNames;
import de.pfiva.data.model.FeedbackType;
import de.pfiva.data.model.PfivaConfigData;
import de.pfiva.data.model.Tuple;
import de.pfiva.data.model.notification.Data.DataType;
import de.pfiva.data.model.notification.FeedbackData;
import de.pfiva.data.model.snips.SnipsOutput;

@Service
public class FeedbackService {

	@Autowired RestTemplate restTemplate;
	@Autowired DataIngestionProperties properties;
	@Autowired NLUDataIngestionDBService dbService;
	@Autowired ObjectMapper objectMapper;
	@Autowired FirebaseService firebaseService;
	
	private static Logger logger = LoggerFactory.getLogger(FeedbackService.class);
	
	public void sendFeedback(SnipsOutput snipsOutput, int queryId, String username) {
		// 1. Generate Feedback
		// 2. Push feedback to db
		// 3. Send Feedback
		if(username != null && !username.isEmpty()) {
			Tuple<FeedbackType, String> feeback = generateFeedbackQuery(snipsOutput);
			int feedbackId = pushFeedbackToDB(feeback, queryId);
			postFeedbackToFirebase(feeback, feedbackId, snipsOutput.getInput(), username);			
		}
	}
	
	private int pushFeedbackToDB(Tuple<FeedbackType, String> feeback, int queryId) {
		int feedbackId = 0;
		if(feeback != null) {
			feedbackId = dbService.pushFeedbackToDB(queryId, feeback.getY());
		}
		return feedbackId;
	}

	private Tuple<FeedbackType, String> generateFeedbackQuery(SnipsOutput snipsOutput) {
		// Feedback query can be generated by: 
		// 1. knowing the intent
		// 2. general feedback
		
		Tuple<FeedbackType, String> feedback = null;
		if(snipsOutput !=null) {
			String userQuery = snipsOutput.getInput();
			if(userQuery != null && !userQuery.trim().isEmpty()) {
				if(snipsOutput.getIntent() != null) {
					String feedbackQuery = "Did you ask about "
							+ parseIntentToUserReadableString(snipsOutput
									.getIntent().getIntentName())
							+ "?";
					feedback = new Tuple<FeedbackType, 
							String>(FeedbackType.INTENT_CLASSIFIED, feedbackQuery);
				} else {
					PfivaConfigData configData = dbService
							.getConfigurationValue(Constants.PFIVA_DEFAULT_FEEDBACK_QUERY);
					feedback = new Tuple<FeedbackType,
							String>(FeedbackType.GENERAL, configData.getValue());
				}
			}
			logger.info("Feedback generated of type [" + feedback.getX() + "],"
					+ " feedback query [" + feedback.getY() + "]");
		}
		return feedback;
	}
	
	private void postFeedbackToFirebase(Tuple<FeedbackType,
			String> feeback, int feedbackId, String userQuery, String username) {
		
		FeedbackData data = new FeedbackData();
		data.setDatatype(DataType.FEEDBACK);
		data.setFeedbackId(feedbackId);
		data.setFeedbackType(feeback.getX());
		data.setText(feeback.getY());
		data.setUserQuery(userQuery);
		
		String clientName = getDeviceId(username);
		if(clientName == null || clientName.isEmpty()) {
			logger.error("User does not have any registered device.");
			throw new IllegalArgumentException("Invalid user.");
		}
		firebaseService.sendRequestToFirebaseServer(data, clientName);		
	}
	
	private String getDeviceId(String username) {
		return dbService.getDeviceId(username);
	}

	private String parseIntentToUserReadableString(String intentName) {
		String parsedIntent = null;
		if(intentName != null && !intentName.trim().isEmpty()) {
			switch (intentName) {
			case IntentNames.SEARCHWEATHERFORECAST:
				parsedIntent = "Weather Forecast";
				break;
			case IntentNames.SEARCH_WIKIPEDIA:
				parsedIntent = "Wikipedia Search";
				break;
			case IntentNames.CONVERT_CURENCY:
				parsedIntent = "Currency Conversion";
				break;
			case IntentNames.GET_NEWS:
				parsedIntent = "News";
				break;
			case IntentNames.TELL_TIME_DATE:
				parsedIntent = "Date/Time";
				break;
			default:
				parsedIntent = IntentNames.UNKNOWN_INTENT;
				break;
			}
		}
		return parsedIntent;
	}

}
