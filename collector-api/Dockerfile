FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/collector-api-*.jar app.jar
EXPOSE 8080
EXPOSE 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]