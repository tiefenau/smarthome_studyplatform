import speech_recognition as sr
import logging
import requests
from argparse import ArgumentParser
import json
import datetime
import os
import RPi.GPIO as GPIO

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')
logger = logging.getLogger(__name__)

r = sr.Recognizer()
m = sr.Microphone()

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(18,GPIO.OUT)

def forwardRequestToPfivaSpeechClient(serverAddress, audioRawFilename, api, language, user):
	try:
		url = 'http://' + serverAddress + ':9001/data/ingestion/speech-to-text'
		with open(audioRawFilename, 'rb') as f:
			files = {'file' : f}
			requests.post(url, files=files, data={"api": api, "language": language, "user": user})
		#requests.post(url, data={"fileName": fileName, "api": api, "language": language, "user": user})
		logger.info('Request forwarded to PFIVA server')
	except requests.ConnectionError as e:
		logger.error('Error forwarding request to PFIVA server {0}'. format(e))


def parseLanguageForSpeech(speechLanguage):
	speechLanguage = speechLanguage.lower();

	if speechLanguage == 'english':
		return 'en-US'
	elif speechLanguage == 'german':
		return 'de-DE'
	elif speechLanguage == 'french':
		return 'fr-FR'
	elif speechLanguage == 'hindi':
		return 'hi-IN'
	else:
		logger.error('Language not supported')
		exit()


def main(speechLanguage, api, user, serverAddress):
	try:
		audioDirectoryPath = '/Users/rahullao/audioDumps'
		try:
			if not os.path.exists(audioDirectoryPath):
				os.mkdir(audioDirectoryPath)
				logger.info('Audio directory does not exists, created')
			else:
				logger.info('Audio directory already exists')
		except OSError:
			logger.info('Error creating directory ' + audioDirectoryPath)
			exit()

		logger.info('Initiating speech recognition.')
		with m as source: r.adjust_for_ambient_noise(source)
		logger.info('Setting minimum energy threshold to [' + format(r.energy_threshold) + ']')
		while True:
			GPIO.output(18,GPIO.HIGH)
			logger.info('Waiting for user query')
			# After 5 secs, cut off listening
			with m as source: audio = r.listen(source, phrase_time_limit=5)
			if audio is not None:
				GPIO.output(18,GPIO.LOW)
				logger.info('Audio captured, getting raw and wav data.')
				rawData = audio.get_raw_data(convert_rate=16000, convert_width=2)
				wavData = audio.get_wav_data(convert_rate=16000, convert_width=2)

				currentDateTime = datetime.datetime.now().strftime("%Y-%m-%dT%H_%M_%S")
				try:
					path = audioDirectoryPath + '/' + currentDateTime
					logger.info('Creating directory : ' + path) 
					os.mkdir(path)
				except OSError:
					logger.info('Error creating directory ' + path)
					exit()

				outputRawFilename = path + "/" + currentDateTime + ".raw"
				outputRawFile = open(outputRawFilename,"wb")
				outputRawFile.write(rawData)
				outputRawFile.close()
				logger.info('Audio data written to a raw file')
				
				outputWavFilename = path + "/" + currentDateTime + ".wav"
				outputWavFile = open(outputWavFilename,"wb")
				outputWavFile.write(wavData)
				outputWavFile.close()
				logger.info('Audio data written to a wav file')

				#forwardRequestToPfivaSpeechClient(serverAddress, currentDateTime, api, parseLanguageForSpeech(speechLanguage), user)
				forwardRequestToPfivaSpeechClient(serverAddress, outputRawFilename, api, parseLanguageForSpeech(speechLanguage), user)
	except KeyboardInterrupt:
		pass


if __name__ == '__main__':
	parser = ArgumentParser()
	parser.add_argument("--language", dest="language", help="Specify language for voice assistant", required=False, default="english")
	parser.add_argument("--api", dest="api", help="Specify API for voice assistant", required=False, default="GoogleCloudSpeech")
	parser.add_argument("--user", dest="user", help="Specify user for voice assistant", required=True)
	parser.add_argument("--serverAddress", dest="serverAddress", help="Specify address for PFIVA server", required=True)
	args = parser.parse_args()
	logger.info('Language : ' + str(args.language))
	logger.info('API : ' + str(args.api))
	logger.info('User : ' + str(args.user))
	logger.info('PFIVA server address : ' + str(args.serverAddress))
	main(args.language, args.api, args.user, args.serverAddress)
