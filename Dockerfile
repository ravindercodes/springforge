# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the generated JAR file to the container
COPY target/spring-boot-project-starter.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with dynamic environment variables
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "app.jar"]
