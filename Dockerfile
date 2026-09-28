# Step 1: Build the Maven application using the modern Temurin JDK
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Lightweight runtime environment using Eclipse Temurin 
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Crucial JVM flags for constrained 512MB RAM free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
