package de.pfiva.data.ingestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.model.PfivaConfigData;

@Configuration
@AutoConfigureAfter(DataIngestionConfiguration.class)
@ConditionalOnBean(NLUDataIngestionDBService.class)
public class PfivaConfiguration {

	@Autowired NLUDataIngestionDBService dbService;
	
	@Bean
	public ConfigProperties getConfigProperties() {
		List<PfivaConfigData> configValues = dbService.getConfigurationValues();
		Map<String, String>  properties = new HashMap<>();
		for(PfivaConfigData data : configValues) {
			properties.put(data.getKey(), data.getValue());
		}
		ConfigProperties configProperties = new ConfigProperties(properties);
		return configProperties;
	}
}
