FROM python:3.6
COPY . /app
WORKDIR /app
#RUN curl -f -L https://static.rust-lang.org/rustup.sh -O
#RUN sh rustup.sh --disable-sudo
RUN pip install --no-cache-dir -r requirements.txt
RUN snips-nlu download en
EXPOSE 5000
CMD ["python", "./nlu_application.py"]