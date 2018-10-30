package de.pfiva.speech.text.client.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

@Service
public class GoogleCloudService {

	private static Logger logger = LoggerFactory.getLogger(GoogleCloudService.class);
	
	public String getSpeechToText(String fileName, String language) {
		String outputText = null;
		try (SpeechClient speechClient = SpeechClient.create()) {

			// The path to the audio file to transcribe
			String filePath = "/Users/rahullao/audioDumps/" + fileName + "/" + fileName + ".raw";

			// Reads the audio file into memory
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);

			// Builds the sync recognize request
			RecognitionConfig config = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.LINEAR16)
					.setSampleRateHertz(16000)
					.setLanguageCode(language)
					.build();
			
			RecognitionAudio audio = RecognitionAudio.newBuilder()
					.setContent(audioBytes)
					.build();

			// Performs speech recognition on the audio file
			RecognizeResponse response = speechClient.recognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech. Just use the
				// first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				outputText = alternative.getTranscript();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputText;
	}
}
