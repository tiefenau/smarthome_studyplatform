FROM ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
  python2.7 \
  portaudio19-dev \
  python-all-dev \
  python-pyaudio \
  python-pip \
  git-all \
  flac
COPY . /app
WORKDIR /app
RUN pip install --no-cache-dir -r requirements.txt
CMD ["python", "./assistant3.0.py"]