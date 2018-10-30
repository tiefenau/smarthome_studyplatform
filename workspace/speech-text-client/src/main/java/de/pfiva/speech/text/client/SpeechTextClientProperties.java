package de.pfiva.speech.text.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class SpeechTextClientProperties {

	private String dataIngestionPipelineUrl;

	public String getDataIngestionPipelineUrl() {
		return dataIngestionPipelineUrl;
	}

	public void setDataIngestionPipelineUrl(String dataIngestionPipelineUrl) {
		this.dataIngestionPipelineUrl = dataIngestionPipelineUrl;
	}
}
