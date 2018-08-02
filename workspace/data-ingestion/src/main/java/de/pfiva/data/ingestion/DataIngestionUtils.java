package de.pfiva.data.ingestion;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DataIngestionUtils {
	
	public static LocalDateTime extractTimestamp(String filename) {
		// sample file name - 2018-07-11T18_18_23.608512558+00_00-hotword-hey_snips.json
		LocalDateTime sqlDateTime = null;
		if(filename != null && !filename.trim().isEmpty()) {
			String timestamp = filename.substring(0, filename.indexOf('+'));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.INPUT_FILE_TIME_FORMAT);
			LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
			
			DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
			sqlDateTime = LocalDateTime.parse(localDateTime.format(sqlFormatter), sqlFormatter);
//			Date date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
			
//			SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
//			try {
//				parsedDated = simpleDateFormatter.parse(date.toString());
//			} catch (ParseException e) {
//				logger.error("Error parsing date", e);
//			}
		}
		return sqlDateTime;
	}
	
	public static Map<String, String> parseResponseBody(String requestBody) throws UnsupportedEncodingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		String decode = URLDecoder.decode(requestBody, "UTF-8");
		String[] keyValuePairs = decode.split("&");
		for(String pair : keyValuePairs) {
			String[] entry = pair.split("=");
			responseMap.put(entry[0].trim(), entry[1].trim());
		}
		return responseMap;
	}
}
