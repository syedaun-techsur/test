# Spring Boot Demo Project

A simple Spring Boot application demonstrating basic REST API endpoints.

## Features

- REST API endpoints
- JSON response handling
- Path variables and request body processing
- Basic application information endpoint

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### Running the Application

1. Clone or download this project
2. Navigate to the project directory
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Endpoints

#### GET /api/hello
Returns a simple greeting message.

**Response:**
```
Hello, Spring Boot!
```

#### GET /api/hello/{name}
Returns a personalized greeting.

**Example:** `GET /api/hello/John`
**Response:**
```
Hello, John!
```

#### POST /api/greet
Accepts a JSON request with a name and returns a greeting with timestamp.

**Request Body:**
```json
{
  "name": "Alice"
}
```

**Response:**
```json
{
  "message": "Hello, Alice!",
  "timestamp": "2024-01-15T10:30:45.123"
}
```

#### GET /api/info
Returns application information.

**Response:**
```json
{
  "application": "Spring Boot Demo",
  "version": "1.0.0",
  "status": "running",
  "timestamp": "2024-01-15T10:30:45.123"
}
```

## Testing the API

You can test the endpoints using curl:

```bash
# Test hello endpoint
curl http://localhost:8080/api/hello

# Test hello with name
curl http://localhost:8080/api/hello/World

# Test info endpoint
curl http://localhost:8080/api/info

# Test POST endpoint
curl -X POST http://localhost:8080/api/greet \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User"}'
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── demo/
│   │               ├── DemoApplication.java
│   │               └── controller/
│   │                   └── HelloController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── example/
                └── demo/
                    └── DemoApplicationTests.java
```

## Building the Project

To build the project:

```bash
mvn clean package
```

This will create a JAR file in the `target/` directory that you can run with:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
``` 