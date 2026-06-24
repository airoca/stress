FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar stress.jar
EXPOSE 8080
CMD ["java", "-jar", "stress.jar"]