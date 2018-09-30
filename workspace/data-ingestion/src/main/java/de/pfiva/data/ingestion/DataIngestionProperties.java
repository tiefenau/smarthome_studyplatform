package de.pfiva.data.ingestion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "data.ingestion")
public class DataIngestionProperties {

	private boolean snipsWatch; //TODO - remove
	private String hotword; //TODO - remove
	private String databaseName;
	private String databaseUrl;
	private boolean feedbackAfterIntentClassification; //TODO - remove
	
	private String firebaseUrl;
	private String mobileFirebaseServerKey; //TODO - remove
	private String mobileClientName; //TODO - remove
	private String wearClientName; //TODO - remove
	private String wearFirebaseServerKey; //TODO - remove
	
	private NotificationClient notificationClient;
	
	private String defaultFeedbackQuery;

	public enum NotificationClient {
		MOBILE, WEAR;
	}
	
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

	public String getMobileFirebaseServerKey() {
		return mobileFirebaseServerKey;
	}

	public void setMobileFirebaseServerKey(String mobileFirebaseServerKey) {
		this.mobileFirebaseServerKey = mobileFirebaseServerKey;
	}

	public String getMobileClientName() {
		return mobileClientName;
	}

	public void setMobileClientName(String mobileClientName) {
		this.mobileClientName = mobileClientName;
	}

	public String getWearFirebaseServerKey() {
		return wearFirebaseServerKey;
	}

	public void setWearFirebaseServerKey(String wearFirebaseServerKey) {
		this.wearFirebaseServerKey = wearFirebaseServerKey;
	}

	public String getWearClientName() {
		return wearClientName;
	}

	public void setWearClientName(String wearClientName) {
		this.wearClientName = wearClientName;
	}
	
	public NotificationClient getNotificationClient() {
		return notificationClient;
	}

	public void setNotificationClient(NotificationClient notificationClient) {
		this.notificationClient = notificationClient;
	}

	public String getDefaultFeedbackQuery() {
		return defaultFeedbackQuery;
	}

	public void setDefaultFeedbackQuery(String defaultFeedbackQuery) {
		this.defaultFeedbackQuery = defaultFeedbackQuery;
	}
}
