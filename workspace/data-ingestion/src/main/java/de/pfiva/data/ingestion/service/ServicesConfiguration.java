package de.pfiva.data.ingestion.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//@ConditionalOnMissingBean
public class ServicesConfiguration {
	
//	@Bean
//	public FileExtractor createFileExtractor(){
//		return new FileExtractor();
//	}
	
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
}
