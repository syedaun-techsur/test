FROM gradle:jdk21 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build the application without running tests and without starting the daemon
RUN chmod +x ./gradlew && ./gradlew build --no-daemon -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/

CMD ["java", "-jar", "/app/rfpai-service-plain.jar"]
