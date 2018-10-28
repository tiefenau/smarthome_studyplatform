import speech_recognition as sr
import logging
import requests
from argparse import ArgumentParser
import json
import datetime
import os

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')
logger = logging.getLogger(__name__)

r = sr.Recognizer()
m = sr.Microphone()

def forwardRequestToPfivaSpeechClient(fileName, api, language, user):
	try:
		requests.post('http://192.168.1.205:9002/pfiva/speech-to-text', data={"fileName": fileName, "api": api, "language": language, "user": user})
		logger.info('Request forwarded to pfiva speech-to-text client')
	except:
		logger.error('Error forwarding request to pfiva speech-to-text client {0}'. format(e))


def forward_query_to_data_ingestion(userQuery, user):
    try:
        response = requests.post('http://10.148.246.190:9001/data/ingestion/user-query', data={'userQuery': userQuery, 'username': user})
        logger.info('Request forwarded to data ingestion with user query [' + userQuery + ']')
    except requests.ConnectionError as e:
        logger.error('Error forwarding request to data-ingestion pipeline {0}'. format(e))


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


def main(speechLanguage, api, user):
	try:
		logger.info('Initiating speech recognition.')
		with m as source: r.adjust_for_ambient_noise(source)
		logger.info('Setting minimum energy threshold to [' + format(r.energy_threshold) + ']')
		while True:
			logger.info('Waiting for user query')
			with m as source: audio = r.listen(source)
			if audio is not None:
				logger.info('Audio captured, getting raw and wav data.')
				rawData = audio.get_raw_data(convert_rate=16000, convert_width=2)
				wavData = audio.get_wav_data(convert_rate=16000, convert_width=2)

				currentDateTime = datetime.datetime.now().strftime("%Y-%m-%dT%H_%M_%S")
				try:
					path = 'audioDumps/' + currentDateTime
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

				forwardRequestToPfivaSpeechClient(currentDateTime+".raw", api, parseLanguageForSpeech(speechLanguage), user)
	except KeyboardInterrupt:
		pass


if __name__ == '__main__':
	parser = ArgumentParser()
	parser.add_argument("-l", "--language", dest="language", help="Specify language for voice assistant", required=True)
	parser.add_argument("-a", "--api", dest="api", help="Specify API for voice assistant", required=True)
	parser.add_argument("-u", "--user", dest="user", help="Specify user for voice assistant", required=True)
	args = parser.parse_args()
	logger.info('Language : ' + str(args.language))
	logger.info('API : ' + str(args.api))
	logger.info('User : ' + str(args.user))
	main(args.language, args.api, args.user)
