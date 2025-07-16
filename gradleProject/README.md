# Simple Spring Boot Application

A simple Spring Boot application built with Gradle.

## Features

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- RESTful API endpoints
- User management functionality
- Actuator for monitoring

## Prerequisites

- Java 17 or higher
- Gradle (or use the included wrapper)

## Running the Application

1. **Build the project:**
   ```bash
   ./gradlew clean build
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the application:**
   - Main page: http://localhost:8080
   - Hello endpoint: http://localhost:8080/hello
   - Users API: http://localhost:8080/api/users
   - H2 Console: http://localhost:8080/h2-console

## API Endpoints

- `GET /` - Welcome message
- `GET /hello?name=YourName` - Hello message
- `GET /api/users` - Get all users
- `POST /api/users` - Create a new user
- `GET /api/users/{id}` - Get user by ID

## Database

The application uses H2 in-memory database. You can access the H2 console at:
http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Testing

Run tests with:
```bash
./gradlew test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── SimpleSpringBootApplication.java
│   │   ├── controller/
│   │   │   ├── HelloController.java
│   │   │   └── UserController.java
│   │   ├── entity/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   └── service/
│   │       └── UserService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/
        └── SimpleSpringBootApplicationTests.java
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Boot Starter Actuator
- H2 Database
- Lombok
- Spring Boot Starter Test 