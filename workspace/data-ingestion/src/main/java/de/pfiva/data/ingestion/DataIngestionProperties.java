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
}
