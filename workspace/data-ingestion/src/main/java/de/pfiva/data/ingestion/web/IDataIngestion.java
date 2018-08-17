package de.pfiva.data.ingestion.web;

import java.util.List;

import de.pfiva.data.model.NLUData;

public interface IDataIngestion {

	public void captureFileGeneration(String requestBody);
	
	public void activateSnipsWatch(boolean snipsWatch);
	
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(String requestBody);
	
	public List<NLUData> getCompleteNLUData();
}
