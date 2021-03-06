package de.pfiva.data.ingestion.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

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

public interface IDataIngestion {

	public void getspeechToText(MultipartFile file, String api, 
			String language, String user);
			
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(ClientToken clientToken);
	
	public List<NLUData> getCompleteNLUData(String deviceId);
	
	public ResponseEntity<Boolean> saveFeedbackResponse(Feedback feedback);
	
	public List<User> getUsers();
	
	public boolean addNewUser(User user);
	
	public boolean deleteUser(int userId);
	
	public void sendMessage(Message message);
	
	public boolean cancelScheduledMessage(int messageId);
	
	public List<Message> getMessages(String topic);
	
	public MessageResponseData getCompleteMessageData(int messageId);
	
	public boolean saveMessageResponse(int messageId, Response response);
	
	public List<PfivaConfigData> getConfigurationValues();
	
	public void saveConfigValue(PfivaConfigData configData);
	
	public void sendSurvey(Survey survey);
	
	public boolean cancelScheduledSurvey(int surveyId);
	
	public List<Survey> getSurveys(String topic);
	
	public SurveyResponseData getCompleteSurveyData(int surveyId);
	
	public boolean saveSurveyResponse(int surveyId, List<de.pfiva.data.model.survey.Response> responses);
	
	public List<Topic> getTopics();
	
	public List<String> getTopicNames();
	
	public boolean deleteTopicWithAssociatedData(int topicId);
}
