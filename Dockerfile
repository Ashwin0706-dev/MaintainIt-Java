# Step 1: Build the Maven application natively using Java 25
FROM maven:3-eclipse-temurin-25-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Lightweight runtime environment using official Eclipse Temurin JRE 25
FROM eclipse-temurin:25-jre-jammy
COPY --from=build /target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Crucial JVM flags for constrained 512MB RAM free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
