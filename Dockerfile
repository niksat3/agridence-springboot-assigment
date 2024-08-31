# Use Maven image to build the application
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Use Eclipse Temurin JDK image for the runtime
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/Assignment-0.0.1-SNAPSHOT.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]