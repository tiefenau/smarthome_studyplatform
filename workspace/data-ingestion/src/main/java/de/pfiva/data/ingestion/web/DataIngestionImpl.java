package de.pfiva.data.ingestion.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pfiva.data.ingestion.DataIngestionProperties;
import de.pfiva.data.ingestion.DataIngestionUtils;
import de.pfiva.data.ingestion.model.InputFile;
import de.pfiva.data.ingestion.service.NLUDataIngestionService;
import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.notification.ClientToken;

@RestController
@RequestMapping(value = "data/ingestion")
public class DataIngestionImpl implements IDataIngestion {

	private static Logger logger = LoggerFactory.getLogger(DataIngestionImpl.class);
	
	@Autowired private NLUDataIngestionService dataIngestionService;
	@Autowired private DataIngestionProperties properties;
	
	@Override
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	public void captureFileGeneration(@RequestBody String requestBody) {
		
		InputFile inputFile = null;
		//Begin file parsing only if snips watch is enabled
		if(properties.isSnipsWatch()) {
			try {
				inputFile = parseFileGenerationRequestBody(requestBody);
			} catch (UnsupportedEncodingException e) {
				logger.info("Error while parsing input file details", e);
//			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if(inputFile != null) {
				dataIngestionService.extractInboundFileData(inputFile);
			}			
		}
		
//		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	private InputFile parseFileGenerationRequestBody(String requestBody) throws UnsupportedEncodingException {
		Map<String, String> responseMap = DataIngestionUtils.parseResponseBody(requestBody);
		InputFile inputFile = new InputFile();
		inputFile.setFilename(responseMap.get("filename"));
		inputFile.setFilePath(responseMap.get("filePath"));
		return inputFile;
	}

	@Override
	@RequestMapping(value = "/snips-watch", method = RequestMethod.PUT)
	public void activateSnipsWatch(@RequestParam("snipsWatch") boolean snipsWatch) {
		properties.setSnipsWatch(snipsWatch);
	}

	@Override
	@RequestMapping(value = "/user-query", method = RequestMethod.POST)
	public void captureUserQuery(@RequestBody String requestBody) {
		try {
			Map<String, String> responseMap = DataIngestionUtils.parseResponseBody(requestBody);
			String userQuery = responseMap.get("userQuery");
			if(userQuery != null && !userQuery.isEmpty()) {
				dataIngestionService.extractUserQuery(userQuery);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("Error while parsing input file details", e);
		}
	}

	
	@Override
	@RequestMapping(value = "/client-token", method = RequestMethod.POST)
	public void saveClientRegistrationToken(@RequestBody ClientToken clientToken) {
		String clientName = clientToken.getClientName();
		String token = clientToken.getToken();
		if(token != null && !token.isEmpty()) {
			logger.info("Token received for [" + clientName + "] as [" + token + "].");
			dataIngestionService.saveClientRegistrationToken(clientName, token);
		}
	}
	
	@Override
	@RequestMapping(value = "/nlu-data", method = RequestMethod.GET)
	public List<NLUData> getCompleteNLUData() {
		return dataIngestionService.getCompleteNLUData();
	}

	@Override
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public ResponseEntity<Boolean> saveFeedbackResponse(@RequestBody Feedback feedback) {
		boolean status = false;
		if(feedback != null) {
			status = dataIngestionService.saveFeedbackResponse(feedback);
		}
		return new ResponseEntity<Boolean>(Boolean.valueOf(status), HttpStatus.OK);
	}
}
