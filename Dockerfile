# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY ./target/telegram-0.0.1.jar /app

# Run the application
CMD ["java", "-jar", "telegram-0.0.1.jar"]