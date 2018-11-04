FROM openjdk:8
ADD apiKey/serviceAccount.json serviceAccount.json
ADD target/data-ingestion-0.0.1-SNAPSHOT.jar data-ingestion-0.0.1-SNAPSHOT.jar
ENV GOOGLE_APPLICATION_CREDENTIALS=serviceAccount.json
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "data-ingestion-0.0.1-SNAPSHOT.jar"]