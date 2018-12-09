from os import walk
from datetime import datetime, timedelta
import shutil

def deleteDirectories(audioDirectoryPath, directories):
	currentDateTime = datetime.now().strftime("%Y-%m-%dT%H_%M_%S")
	currentDateObject = datetime.strptime(currentDateTime, '%Y-%m-%dT%H_%M_%S') - timedelta(seconds=15)
	
	for directory in directories:
		directoryDateObject = datetime.strptime(directory, '%Y-%m-%dT%H_%M_%S')
		if directoryDateObject < currentDateObject:
			shutil.rmtree(audioDirectoryPath + '/' + directory)


def main():
	audioDirectoryPath = '/Users/rahullao/audioDumps'
	directories = []
	for (dirpath, dirnames, filenames) in walk(audioDirectoryPath):
		directories.extend(dirnames)
		break

	deleteDirectories(audioDirectoryPath, directories)


if __name__ == '__main__':
	main()