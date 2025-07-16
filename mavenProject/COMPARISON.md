# Gradle vs Maven Comparison

This document compares the Gradle and Maven versions of the same Spring Boot application.

## Project Structure Comparison

| Component         | Gradle Project                       | Maven Project                       |
|-------------------|------------------------------------|-----------------------------------|
| Build File        | `build.gradle`                     | `pom.xml`                         |
| Wrapper Script    | `gradlew`                         | `mvnw`                            |
| Wrapper Properties| `gradle/wrapper/gradle-wrapper.properties` | `.mvn/wrapper/maven-wrapper.properties` |
| Source Structure  | `src/main/java/`                   | `src/main/java/`                  |
| Resources         | `src/main/resources/`              | `src/main/resources/`             |
| Tests             | `src/test/java/`                   | `src/test/java/`                  |

## Build Commands Comparison

| Task              | Gradle Command              | Maven Command        |
|-------------------|----------------------------|----------------------|
| Clean Build       | `./gradlew clean build`    | `./mvnw clean package` |
| Run Application   | `./gradlew bootRun`         | `./mvnw spring-boot:run` |
| Run Tests         | `./gradlew test`            | `./mvnw test`         |
| Package           | `./gradlew bootJar`         | `./mvnw package`      |
| Install           | `./gradlew install`         | `./mvnw install`      |

## Dependency Management

### Gradle (`build.gradle`)