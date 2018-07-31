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
}
