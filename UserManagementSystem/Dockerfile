# Use a Java runtime as the base image (e.g., OpenJDK 17)
# Using a specific version and 'slim' or 'jre' variants helps reduce image size
FROM openjdk:17-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application's packaged JAR file from your local target/ directory into the container
# Replace 'target/your-application-name.jar' with the actual path and name of your JAR file
COPY target/UserManagementService.jar app.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
