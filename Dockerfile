# Use an official Gradle image to build the application
FROM gradle:8.12.1-jdk21-alpine AS build

# Set the working directory
WORKDIR /home/gradle/project

# Copy the project files
COPY --chown=gradle:gradle . .

# Build the application
RUN gradle build --no-daemon -x test

# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:21-alpine3.21

# Set the working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080
EXPOSE 9091

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=secrets,${SPRING_PROFILES_ACTIVE}", "--jasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}"]