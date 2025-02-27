FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/payment_service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]