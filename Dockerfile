# =========
# Build stage
# =========
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /workspace
# Leverage Docker layer caching by copying pom first
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -q -e -DskipTests package

# =========
# Runtime stage
# =========
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app
# Copy the fat jar from the builder stage
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Optional: Pass extra JVM options at runtime via JAVA_OPTS
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -XX:+ExitOnOutOfMemoryError"

# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
