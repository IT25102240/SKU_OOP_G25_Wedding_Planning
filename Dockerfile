# ============================================
# EverAfter — Wedding Planning & Vendor Booking
# Multi-stage Docker build for free-tier hosting
# ============================================

# --- Stage 1: Build ---
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Install Maven (project has no Maven wrapper)
RUN apk add --no-cache maven

# Copy pom first for better layer caching
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the JAR (skip tests for faster build)
RUN mvn package -DskipTests

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
