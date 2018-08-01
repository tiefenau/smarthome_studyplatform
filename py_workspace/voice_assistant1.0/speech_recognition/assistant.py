import speech_recognition as sr
import logging
import requests

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

try:
    logger.info('Initiating speech recognition.')
    with m as source: r.adjust_for_ambient_noise(source)
    logger.info('Setting minimum energy threshold to [' + format(r.energy_threshold) + ']')
    while True:
        logger.info('Waiting for user query')
        with m as source: audio = r.listen(source)
        logger.info('Audio captured, begin speech to text.')
        try:
            # recognize speech using Google Speech Recognition
            value = r.recognize_google(audio)
            logger.info('Speech to text recognition done.')
            # we need some special handling here to correctly print unicode characters to standard output
            if str is bytes:  # this version of Python uses bytes for strings (Python 2)
                logger.info('You said {}'.format(value).encode("utf-8"))
                forward_query_to_data_ingestion(format(value).encode("utf-8"))
            else:  # this version of Python uses unicode for strings (Python 3+)
                logger.info('You said {}'.format(value))
                forward_query_to_data_ingestion(format(value))
        except sr.UnknownValueError:
            logger.error('Unable to catch user speech.')
        except sr.RequestError as e:
            logger.error('Error fetching results from Google Speech Recognition service {0}'.format(e))
except KeyboardInterrupt:
    pass
