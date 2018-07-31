package de.pfiva.data.ingestion.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.ingestion.DataIngestionUtils;
import de.pfiva.data.ingestion.model.InputFile;
import de.pfiva.data.ingestion.model.snips.InboundQueryData;
import de.pfiva.data.ingestion.model.snips.SnipsOutput;

@Service
public class FileExtractorService {

	@Autowired private ObjectMapper objectMapper;
	
	private static Logger logger = LoggerFactory.getLogger(FileExtractorService.class);
	
	public InboundQueryData extractInboundFileData(InputFile inputFile) {
		// Snips generates 5 files. 2 for hotword, 
		// 3 for user query and intent classification. We are interested in
		// hotword json file and user query .nlu.json file
		
		logger.info("Extracting file [" + inputFile.getFilename() + "].");
		return extractFileByType(inputFile.getFilename(), inputFile.getFilePath());
	}

	private InboundQueryData extractFileByType(String filename, String filePath) {
		InboundQueryData inboundQueryData = null;
		try {
			if(filename.matches(Constants.QUERY_NLU_JSON_REGX)) {
				logger.info("File type - query nlu json");
				inboundQueryData = objectMapper.readValue(new File(filePath.concat(filename)), SnipsOutput.class);
			} else {
				logger.info("Couldn't extract file [" + filename + "], as regx match didn't succeeded.");
			}
			
			if(inboundQueryData != null) {
				inboundQueryData.setFilename(filename);
				inboundQueryData.setFilePath(filePath);
				inboundQueryData.setTimestamp(DataIngestionUtils.extractTimestamp(filename));				
			}
			
		} catch(JsonParseException e) {
			logger.error("Error reading file", e);
		} catch (JsonMappingException e) {
			logger.error("Error reading file", e);
		} catch(IOException e) {
			logger.error("Error reading file", e);
		}
		return inboundQueryData;
	}
}
