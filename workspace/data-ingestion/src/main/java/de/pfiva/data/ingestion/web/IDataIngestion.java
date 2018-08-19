package de.pfiva.data.ingestion.web;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;

public interface IDataIngestion {

	public void captureFileGeneration(String requestBody);
	
	public void activateSnipsWatch(boolean snipsWatch);
	
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(String requestBody);
	
	public List<NLUData> getCompleteNLUData();
	
	public ResponseEntity<Boolean> saveFeedbackResponse(Feedback feedback);
}
