# Step 1: Build the Maven application with Java 25 manually injected
FROM maven:3.9-alpine AS build
COPY --from=eclipse-temurin:25 /opt/java/openjdk /opt/java/openjdk
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY . .
RUN mvn clean package -DskipTests

# Step 2: Runtime environment using official Eclipse Temurin JRE 25
FROM eclipse-temurin:25-jre-jammy
COPY --from=build /target/MaintainIt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Crucial JVM flags for constrained 512MB RAM free tiers
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
