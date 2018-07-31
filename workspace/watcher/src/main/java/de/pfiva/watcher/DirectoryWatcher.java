package de.pfiva.watcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DirectoryWatcher {

	private static Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);
//	public static final String INBOUND_PATH = "C:\\Users\\rahul.lao\\Downloads\\directoryWatcher\\";
	public static final String INBOUND_PATH = "/home/pi/Documents/SnipsDumps/";
//	public static final String INBOUND_PATH = "/Users/rahullao/Documents/Master Thesis/directoryWatch/";
	private static final String URL = "http://localhost:9001/data/ingestion/file";
	
	public static void main(String[] args) {
		SpringApplication.run(DirectoryWatcher.class, args);
		initiateWatcher();
	}

	private static void initiateWatcher() {
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.error("Error while creating watch service", e);
		}
		
		Path path = Paths.get(INBOUND_PATH);
		try {
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			logger.error("Error while registering path in watch service", e);
		}
		
		WatchKey key;
		try {
			while((key = watchService.take()) != null) {
				for(WatchEvent<?> event : key.pollEvents()) {
					logger.info("Publishing request for file [" +event.context() +"]");
					publishHttpRequest(String.valueOf(event.context()), INBOUND_PATH);
				}
				key.reset();
			}
		} catch (InterruptedException e) {
			logger.error("Error while publishing request for file", e);
		}
	}

	private static void publishHttpRequest(String filename, String inboundPath) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("filename", filename);
		parameters.put("filePath", INBOUND_PATH);
		
		URL url;
		HttpURLConnection con = null;
		try {
			String urlParameters = getParamsString(parameters);
			url = new URL(URL);
			con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			con.setRequestProperty("Content-Language", "en-US");
			
			con.setUseCaches(false);
			con.setDoOutput(true);
			
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(urlParameters);
//			out.flush();
			out.close();
			logger.info("Request forward at [" + URL + "], "
					+ "with parameters - filename[" + filename + "],"
							+ " filePath[" + INBOUND_PATH + "].");
			
			//Get response
			InputStream is = con.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    logger.info(response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
	
	public static String getParamsString(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0
				? resultString.substring(0, resultString.length() - 1)
						: resultString;
	}
}
