FROM maven:3.8.5-openjdk-17 AS build
COPY . .

RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim


# Step 4: Copy the JAR file into the container
COPY --from=build target/email-service-0.0.1-SNAPSHOT.jar email.jar

# Step 5: Expose the application port
EXPOSE 8080

# Step 6: Run the application
ENTRYPOINT ["java", "-jar", "email.jar"]
