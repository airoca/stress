FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar mvest-order.jar
EXPOSE 9001
CMD ["java", "-jar", "mvest-order.jar"]