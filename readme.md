# Spring WebFlux Demo

A practical example of building a reactive, non-blocking REST API with Spring WebFlux and MongoDB.

**For the full context on when and why to use WebFlux, [read the blog post](link-to-your-blog).**

## Quick Start

### Prerequisites
- Docker & Docker Compose (easiest way)
- Or: Java 21, Maven 3.9+, and MongoDB running locally

### With Docker Compose

```bash
docker-compose up
```

The app runs on `http://localhost:8080`.

### Without Docker

```bash
# Start MongoDB
mongod

# Build and run
mvn clean package
java -jar target/webflux-0.0.1-SNAPSHOT.jar
```

## API Endpoints

```
GET    /api/users           - Get all users
GET    /api/users/{id}      - Get user by ID
POST   /api/users           - Create user
PUT    /api/users/{id}      - Update user
DELETE /api/users/{id}      - Delete user
```

## Project Structure

```
src/main/java/com/example/
├── model/           - User domain model
├── repository/      - Reactive data access
├── service/         - Business logic with Mono/Flux
├── controller/      - REST endpoints
└── exception/       - Global error handling
```

## Key Concepts

This example demonstrates:
- **Mono & Flux**: Non-blocking streams
- **flatMap & switchIfEmpty**: Chaining async operations
- **Reactive error handling**: Using `onErrorResume`
- **Testing**: WebTestClient for integration tests

See the blog for detailed explanations and common pitfalls to avoid.

## Running Tests

```bash
mvn test
```

Uses embedded MongoDB for isolation.