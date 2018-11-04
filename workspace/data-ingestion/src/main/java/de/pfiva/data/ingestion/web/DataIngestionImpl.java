package de.pfiva.data.ingestion.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.pfiva.data.ingestion.DataIngestionUtils;
import de.pfiva.data.ingestion.service.NLUDataIngestionService;
import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.PfivaConfigData;
import de.pfiva.data.model.Topic;
import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Message;
import de.pfiva.data.model.message.MessageResponseData;
import de.pfiva.data.model.message.Response;
import de.pfiva.data.model.notification.ClientToken;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.data.model.survey.SurveyResponseData;

@CrossOrigin
@RestController
@RequestMapping(value = "data/ingestion")
public class DataIngestionImpl implements IDataIngestion {

	private static Logger logger = LoggerFactory.getLogger(DataIngestionImpl.class);
	
	@Autowired private NLUDataIngestionService dataIngestionService;
	
	@Override
	@RequestMapping(value = "/speech-to-text", method = RequestMethod.POST)
	public void getspeechToText(@RequestParam("file") MultipartFile file,
			@RequestParam("api") String api,
			@RequestParam("language") String language,
			@RequestParam("user") String user) {
		
		logger.info("Received new request, [" + file.getOriginalFilename() + ", "
				+ api + ", " + language + ", " + user + "]");
		
		String userQuery = dataIngestionService.getSpeechToText(file, language);
		logger.info("Text captured : " + userQuery);
		if(userQuery != null) {
			dataIngestionService.extractUserQuery(userQuery, user);
		}
	}
	
	@Override
	@RequestMapping(value = "/user-query", method = RequestMethod.POST)
	public void captureUserQuery(@RequestBody String requestBody) {
		try {
			Map<String, String> responseMap = DataIngestionUtils.parseResponseBody(requestBody);
			String userQuery = responseMap.get("userQuery");
			String username = responseMap.get("username");
			if(userQuery != null && !userQuery.isEmpty()) {
				dataIngestionService.extractUserQuery(userQuery, username);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("Error while parsing input file details", e);
		}
	}

	
	@Override
	@RequestMapping(value = "/client-token", method = RequestMethod.POST)
	public void saveClientRegistrationToken(@RequestBody ClientToken clientToken) {
		String clientName = clientToken.getClientName();
		String token = clientToken.getToken();
		if((clientName == null || clientName.isEmpty()) ||
				(token == null || token.isEmpty())) {
			throw new IllegalArgumentException("Clientname or token for a client cannot be empty");
		}
		
		logger.info("Token received for [" + clientName + "] as [" + token + "].");
		dataIngestionService.saveClientRegistrationToken(clientName, token);
	}
	
	@Override
	@RequestMapping(value = "/nlu-data", method = RequestMethod.GET)
	public List<NLUData> getCompleteNLUData(@RequestParam(name="deviceId",
		required=false) String deviceId) {
		if(deviceId == null || deviceId.isEmpty()) {
			return dataIngestionService.getCompleteNLUData();
		} else {
			return dataIngestionService.getCompleteNLUDataByUser(deviceId);
		}
	}

	@Override
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public ResponseEntity<Boolean> saveFeedbackResponse(@RequestBody Feedback feedback) {
		boolean status = false;
		if(feedback != null) {
			status = dataIngestionService.saveFeedbackResponse(feedback);
		}
		return new ResponseEntity<Boolean>(Boolean.valueOf(status), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return dataIngestionService.getUsers();
	}

	@Override
	@RequestMapping(value = "/send-message", method = RequestMethod.POST)
	public void sendMessage(@RequestBody Message message) {
		if(message != null) {
			logger.info("New message received [" + message + "]");
			dataIngestionService.sendMessage(message);
		}
	}

	@Override
	@RequestMapping(value = "/cancel-message/{messageId}", method = RequestMethod.PUT)
	public boolean cancelScheduledMessage(@PathVariable int messageId) {
		return dataIngestionService.cancelScheduledMessage(messageId);
	}

	@Override
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public List<Message> getMessages(@RequestParam(name="topic", required=false) String topic) {
		if(topic == null || topic.isEmpty()) {
			return dataIngestionService.getMessages();
		} else {
			return dataIngestionService.getMessagesByTopic(topic);
		}
	}
	
	@Override
	@RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
	public MessageResponseData getCompleteMessageData(
			@PathVariable int messageId) {
		return dataIngestionService.getCompleteMessageData(messageId);
	}

	@Override
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public boolean addNewUser(@RequestBody User user) {
		if(user != null) {
			return dataIngestionService.addNewUser(user);
		}
		return false;
	}

	@Override
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
	public boolean deleteUser(@PathVariable int userId) {
		return dataIngestionService.deleteUser(userId);
	}
	
	@Override
	@RequestMapping(value = "/config-data", method = RequestMethod.GET)
	public List<PfivaConfigData> getConfigurationValues() {
		return dataIngestionService.getConfigurationValues();
	}

	@Override
	@RequestMapping(value = "/config-data", method = RequestMethod.POST)
	public void saveConfigValue(@RequestBody PfivaConfigData configData) {
		dataIngestionService.saveConfigValue(configData);
	}

	@Override
	@RequestMapping(value = "/send-survey", method = RequestMethod.POST)
	public void sendSurvey(@RequestBody Survey survey) {
		if(survey != null) {
			logger.info("New survey received [" + survey + "]");
			dataIngestionService.sendSurvey(survey);
		}
	}

	@Override
	@RequestMapping(value = "/cancel-survey/{surveyId}", method = RequestMethod.PUT)
	public boolean cancelScheduledSurvey(@PathVariable int surveyId) {
		return dataIngestionService.cancelScheduledSurvey(surveyId);
	}

	@Override
	@RequestMapping(value = "/surveys", method = RequestMethod.GET)
	public List<Survey> getSurveys(@RequestParam(name="topic", required=false) String topic) {
		if(topic == null || topic.isEmpty()) {
			return dataIngestionService.getSurveys();
		} else {
			return dataIngestionService.getSurveysByTopic(topic);
		}
	}

	@Override
	@RequestMapping(value = "/surveys/{surveyId}", method = RequestMethod.GET)
	public SurveyResponseData getCompleteSurveyData(@PathVariable int surveyId) {
		return dataIngestionService.getCompleteSurveyData(surveyId);
	}

	@Override
	@RequestMapping(value = "/message-response/{messageId}", method = RequestMethod.POST)
	public boolean saveMessageResponse(@PathVariable int messageId,
			@RequestBody Response response) {
		return dataIngestionService.saveMessageResponse(messageId, response);
	}

	@Override
	@RequestMapping(value = "/survey-response/{surveyId}", method = RequestMethod.POST)
	public boolean saveSurveyResponse(@PathVariable int surveyId,
			@RequestBody List<de.pfiva.data.model.survey.Response> responses) {
		return dataIngestionService.saveSurveyResponse(surveyId, responses);
	}

	@Override
	@RequestMapping(value = "/topics", method = RequestMethod.GET)
	public List<Topic> getTopics() {
		return dataIngestionService.getTopics();
	}
	
	@Override
	@RequestMapping(value = "/topics-name", method = RequestMethod.GET)
	public List<String> getTopicNames() {
		return dataIngestionService.getTopicNames();
	}
	
	@Override
	@RequestMapping(value = "/topics/{topicId}", method = RequestMethod.DELETE)
	public boolean deleteTopicWithAssociatedData(@PathVariable int topicId) {
		return dataIngestionService.deleteTopicWithAssociatedData(topicId);
	}
}
