package de.pfiva.data.ingestion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "data.ingestion")
public class DataIngestionProperties {

	private String databaseName;
	private String databaseUrl;
	private String dbUser;
	private String dbPassword;
	private String snipsNluEngineUrl;
	private String firebaseUrl;
	
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

	public String getFirebaseUrl() {
		return firebaseUrl;
	}

	public void setFirebaseUrl(String firebaseUrl) {
		this.firebaseUrl = firebaseUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getSnipsNluEngineUrl() {
		return snipsNluEngineUrl;
	}

	public void setSnipsNluEngineUrl(String snipsNluEngineUrl) {
		this.snipsNluEngineUrl = snipsNluEngineUrl;
	}
}
