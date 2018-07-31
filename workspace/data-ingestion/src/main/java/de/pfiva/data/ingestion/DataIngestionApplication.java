package de.pfiva.data.ingestion;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataIngestionApplication {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(DataIngestionApplication.class, args);
	}
}
