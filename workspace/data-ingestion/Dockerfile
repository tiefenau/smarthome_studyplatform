FROM openjdk:8
ADD target/data-ingestion-0.0.1-SNAPSHOT.jar data-ingestion-0.0.1-SNAPSHOT.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "data-ingestion-0.0.1-SNAPSHOT.jar"]