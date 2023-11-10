# Using Amazon Corretto 20 as base image
FROM amazoncorretto:20

RUN mkdir /app

# Define the container working directory
WORKDIR /app

# Copy the jar to the container
COPY seasonsforce-ms-payment-api-1.0-SNAPSHOT.jar /app/app.jar

# Expose the port 8080
EXPOSE 8080

# Command to start the Spring Boot application
CMD ["java", "-jar", "app.jar"]