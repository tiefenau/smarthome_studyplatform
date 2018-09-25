package de.pfiva.data.ingestion;

import java.util.Map;

public class ConfigProperties {

	private Map<String, String> configValues;
	
	public ConfigProperties() {}
	
	public ConfigProperties(Map<String, String> configValues) {
		this.configValues = configValues;
	}

	public Map<String, String> getConfigValues() {
		return configValues;
	}

	public void setConfigValues(Map<String, String> configValues) {
		this.configValues = configValues;
	}
	
}
