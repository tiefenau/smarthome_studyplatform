package de.pfiva.data.ingestion.web;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.User;
import de.pfiva.data.model.notification.ClientToken;

public interface IDataIngestion {

	public void captureFileGeneration(String requestBody);
	
	public void activateSnipsWatch(boolean snipsWatch);
	
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(ClientToken clientToken);
	
	public List<NLUData> getCompleteNLUData();
	
	public ResponseEntity<Boolean> saveFeedbackResponse(Feedback feedback);
	
	public List<User> getUsers();
}
