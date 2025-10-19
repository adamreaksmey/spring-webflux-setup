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
src/main/java/com/example/webflux/
├── WebfluxApplication.java          Main entry point
├── model/
│   └── User.java                    Domain model with validation
├── repository/
│   └── UserRepository.java          Reactive MongoDB data access
├── service/
│   └── UserService.java             Business logic with Mono/Flux chains
├── controller/
│   └── UserController.java          REST endpoints
└── exception/
    └── GlobalExceptionHandler.java  Global error handling
```

## Key Concepts Demonstrated

- **Mono & Flux**: Non-blocking streams that describe async operations
- **flatMap & switchIfEmpty**: Chaining reactive operations together
- **Error handling**: Using `onErrorResume` and custom exception handlers
- **Validation**: Bean validation with `@Valid` and `@NotBlank`, `@Email`
- **Testing**: Integration tests with `WebTestClient`

See the blog post for detailed explanations and common pitfalls to avoid.

## Running Tests

```bash
mvn test
```

Uses embedded MongoDB for test isolation.

## Deployment

The included `Dockerfile` and `docker-compose.yml` handle everything. No manual setup needed.