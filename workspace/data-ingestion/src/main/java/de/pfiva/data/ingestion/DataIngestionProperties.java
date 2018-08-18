package de.pfiva.data.ingestion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "data.ingestion")
public class DataIngestionProperties {

	private boolean snipsWatch;
	private String hotword;
	private String databaseName;
	private String databaseUrl;
	private boolean feedbackAfterIntentClassification;
	
	private String firebaseUrl;
	private String firebaseServerKey;
	private String clientName;

	public boolean isSnipsWatch() {
		return snipsWatch;
	}

	public void setSnipsWatch(boolean snipsWatch) {
		this.snipsWatch = snipsWatch;
	}

	public String getHotword() {
		return hotword;
	}

	public void setHotword(String hotword) {
		this.hotword = hotword;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public boolean isFeedbackAfterIntentClassification() {
		return feedbackAfterIntentClassification;
	}

	public void setFeedbackAfterIntentClassification(boolean feedbackAfterIntentClassification) {
		this.feedbackAfterIntentClassification = feedbackAfterIntentClassification;
	}

	public String getFirebaseUrl() {
		return firebaseUrl;
	}

	public void setFirebaseUrl(String firebaseUrl) {
		this.firebaseUrl = firebaseUrl;
	}

	public String getFirebaseServerKey() {
		return firebaseServerKey;
	}

	public void setFirebaseServerKey(String firebaseServerKey) {
		this.firebaseServerKey = firebaseServerKey;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
