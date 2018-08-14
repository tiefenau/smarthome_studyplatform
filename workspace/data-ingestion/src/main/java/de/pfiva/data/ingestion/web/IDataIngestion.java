package de.pfiva.data.ingestion.web;

public interface IDataIngestion {

	public void captureFileGeneration(String requestBody);
	
	public void activateSnipsWatch(boolean snipsWatch);
	
	public void captureUserQuery(String requestBody);
	
	public void saveClientRegistrationToken(String requestBody);
}
