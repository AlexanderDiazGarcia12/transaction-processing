FROM openjdk:22-jdk-slim
LABEL authors="alexanderdiaz"
VOLUME /tmp
COPY target/transaction-processing-system-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]