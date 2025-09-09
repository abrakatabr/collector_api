FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/collector-api-*.jar app.jar
COPY src/main/resources/notification_template.txt notification_template.txt
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]