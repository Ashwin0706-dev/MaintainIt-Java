# Step 1: Build the Maven application using Eclipse Temurin JDK 25
FROM maven:3.9.9-eclipse-temurin-25 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Lightweight runtime environment using Eclipse Temurin JRE 25
FROM eclipse-temurin:25-jre-jammy
COPY --from=build /target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Crucial JVM flags for constrained 512MB RAM free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
