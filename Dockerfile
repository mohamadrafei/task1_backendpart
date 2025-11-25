# ------------ BUILD STAGE ------------
FROM gradle:8.4-jdk17 AS builder
WORKDIR /workspace

# Copy Gradle files and source
COPY build.gradle settings.gradle ./
COPY src ./src

# Build Spring Boot JAR
RUN gradle bootJar --no-daemon


# ------------ RUNTIME STAGE ------------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
