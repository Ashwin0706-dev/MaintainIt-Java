# Step 1: Build the Maven application natively using stable Java 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Lightweight runtime environment using official Eclipse Temurin JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Crucial JVM flags for constrained 512MB RAM free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
