FROM gradle:jdk21 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew build --no-daemon -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the application jar built by the Gradle build stage
COPY --from=builder /app/build/libs/*.jar ./

# Use non-root user for security best practice
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

LABEL maintainer="solutions.techsur"
LABEL description="RFP AI Service"

ENTRYPOINT ["java", "-jar"]
CMD ["rfpai-service-plain.jar"]