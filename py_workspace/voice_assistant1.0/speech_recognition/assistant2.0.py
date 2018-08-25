import speech_recognition as sr
import logging
import requests
from argparse import ArgumentParser
import json

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')
logger = logging.getLogger(__name__)

r = sr.Recognizer()
m = sr.Microphone()

def forward_query_to_data_ingestion(userQuery):
    try:
        response = requests.post('http://127.0.0.1:9001/data/ingestion/user-query', data={'userQuery': userQuery})
        logger.info('Request forwarded to data ingestion with user query [' + userQuery + ']')
    except requests.ConnectionError as e:
        logger.error('Error forwarding request to data-ingestion pipeline {0}'. format(e))

def parseLanguageForSpeech(speechLanguage):
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

def main(speechLanguage, api):
	try:
		logger.info('Initiating speech recognition.')
		with m as source: r.adjust_for_ambient_noise(source)
		logger.info('Setting minimum energy threshold to [' + format(r.energy_threshold) + ']')
		while True:
			logger.info('Waiting for user query')
			with m as source: audio = r.listen(source)
			logger.info('Audio captured, begin speech to text.')
			value = None
			try:
				if api == 'GoogleSpeech':
					value = r.recognize_google(audio, language=parseLanguageForSpeech(speechLanguage))
				elif api == 'GoogleCloudSpeech':
					credentialFilePath = '/Users/rahullao/Documents/Master Thesis/smarthome_studyplatform/py_workspace/voice_assistant1.0/googleCloudSpeech/serviceAccount.json'
					jsonData = None
					with open(credentialFilePath) as jsonFile:
						jsonData = json.load(jsonFile)
					if jsonData is not None:
						jsonString = json.dumps(jsonData)	
						value = r.recognize_google_cloud(audio, language=parseLanguageForSpeech(speechLanguage), credentials_json=jsonString)
				elif api == 'MicrosoftBingSpeech':
					value = r.recognize_bing(audio, language=parseLanguageForSpeech(speechLanguage))
				elif api == 'Wit':
					value = r.recognize_wit(audio, key='UEZKR2N5KQR4FUEDEHTPPE7KF7QVJSRR')
				else:
					logger.error('API not supported')
					exit()

				logger.info('Speech to text recognition done.')
				logger.info('Speech-to-text translation : ' + value)
				forward_query_to_data_ingestion(format(value).encode("utf-8"))
			except sr.UnknownValueError:
				logger.error('Unable to catch user speech.')
			except sr.RequestError as e:
				logger.error('Error fetching results from Speech Recognition service {0}'.format(e))
	except KeyboardInterrupt:
		pass


if __name__ == '__main__':
	parser = ArgumentParser()
	parser.add_argument("-l", "--language", dest="language", help="Specify language for voice assistant", required=True)
	parser.add_argument("-a", "--api", dest="api", help="Specify API for voice assistant", required=True)
	args = parser.parse_args()
	logger.info('Argument ' + str(args.language))
	logger.info('Argument ' + str(args.api))
	main(args.language, args.api)