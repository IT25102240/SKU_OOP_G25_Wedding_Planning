# ============================================
# EverAfter — Wedding Planning & Vendor Booking
# Multi-stage Docker build for free-tier hosting
# ============================================

# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Use Google's Maven Central mirror to avoid 429 rate limits on shared cloud builders
RUN mkdir -p /root/.m2 && \
    printf '<settings><mirrors><mirror><id>google-maven-central</id><name>GCS Maven Central mirror</name><url>https://maven-central.storage-download.googleapis.com/maven2</url><mirrorOf>central</mirrorOf></mirror></mirrors></settings>' > /root/.m2/settings.xml

# Copy everything needed for the build
COPY pom.xml .
COPY src ./src

# Build the JAR in one step (downloads dependencies + compiles + packages)
RUN mvn package -DskipTests -B

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

# Create data directory for file-based storage
RUN mkdir -p /app/data

# Expose the port
EXPOSE 8080

# JVM flags optimised for free-tier containers (512 MB RAM)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=60 -XX:+UseSerialGC -Xss512k -XX:MaxMetaspaceSize=128m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
