# Use a lightweight base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Expose port
EXPOSE 8080

# Copy the JAR file into the container (flat, clean)
COPY target/expo-management.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

