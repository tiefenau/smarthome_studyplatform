FROM openjdk:8
ADD apiKey/serviceAccount.json serviceAccount.json
ADD target/speech-text-client-0.0.1-SNAPSHOT.jar speech-text-client-0.0.1-SNAPSHOT.jar
ENV GOOGLE_APPLICATION_CREDENTIALS=serviceAccount.json
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "speech-text-client-0.0.1-SNAPSHOT.jar"]