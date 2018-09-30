package de.pfiva.data.ingestion.web;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.PfivaConfigData;
import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Message;
import de.pfiva.data.model.message.MessageResponseData;
import de.pfiva.data.model.message.Response;
import de.pfiva.data.model.notification.ClientToken;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.data.model.survey.SurveyResponseData;

public interface IDataIngestion {

	public void captureFileGeneration(String requestBody);
	
	public void activateSnipsWatch(boolean snipsWatch);
	
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(ClientToken clientToken);
	
	public List<NLUData> getCompleteNLUData();
	
	public ResponseEntity<Boolean> saveFeedbackResponse(Feedback feedback);
	
	public List<User> getUsers();
	
	public boolean addNewUser(User user);
	
	public boolean deleteUser(int userId);
	
	public void sendMessage(Message message);
	
	public boolean cancelScheduledMessage(int messageId);
	
	public List<Message> getMessages();
	
	public MessageResponseData getCompleteMessageData(int messageId);
	
	public boolean saveMessageResponse(int messageId, Response response);
	
	public List<PfivaConfigData> getConfigurationValues();
	
	public void saveConfigValue(PfivaConfigData configData);
	
	public void sendSurvey(Survey survey);
	
	public boolean cancelScheduledSurvey(int surveyId);
	
	public List<Survey> getSurveys();
	
	public SurveyResponseData getCompleteSurveyData(int surveyId);
	
	public boolean saveSurveyResponse(int surveyId, List<de.pfiva.data.model.survey.Response> responses);
}
