# Step 1: Build the application using Maven
FROM maven:3.8.8-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM openjdk:17-jdk-slim
COPY --from=build /target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
