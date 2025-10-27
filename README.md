# Mortgage Service - 1.0.0

A Spring Boot (3.4.x, Java 21) REST API that:
- Exposes mortgage interest rates
- Evaluates mortgage eligibility and calculates a monthly payment
- Uses an in-memory H2 database for persistence and startup seeding
- Provides OpenAPI (Swagger UI) for interactive docs
- Applies global rate limiting and consistent error handling
- Includes extensive unit tests across controllers, services, mappers, rules, repositories, and rate limiting
- Ships with a Dockerfile and an optional remote debugging profile

## Tech Stack

- Java 21, Spring Boot 3.4.x (Web, Data JPA, Validation, Actuator)
- H2 in-memory database
- springdoc-openapi for Swagger UI
- OpenAPI Generator (server code stubs)
- Lombok
- Maven (Surefire for tests)
- Docker (multi-stage build)

Note: The codebase does not use Java preview features. No special JVM flags are required.

## Getting Started

Prerequisites:
- JDK 21
- Maven 3.9+
- Docker (optional)

## Build and Run

- Build and run tests:
  - mvn clean verify
- Build JAR:
  - mvn clean package
- Run locally:
  - mvn spring-boot:run
  - or: java -jar target/mortgage-service-1.0.0.jar

## API

Base path: /api

- GET /api/interest-rates
  - 200 OK with MortgageRatesResponse payload
  - 204 No Content when no rates available
  - 500 Internal Server Error on unexpected failures

- POST /api/mortgage-checking
  - Request: MortgageCheckRequest
    - income: BigDecimal, NotNull, Positive
    - maturityPeriod: Integer, NotNull, Positive (years)
    - loanValue: BigDecimal, NotNull, Positive
    - homeValue: BigDecimal, NotNull, Positive
  - 200 OK with MortgageCheckResponse payload
  - 400 Bad Request on validation/binding errors
  - 500 Internal Server Error on unexpected failures

OpenAPI/Swagger:
- Swagger UI: /swagger-ui/index.html
- OpenAPI JSON: /v3/api-docs

## Data and Seeding

- The application seeds initial interest rates into the in-memory H2 database at startup if the table is empty.

## Error Handling

Handled globally with a controller advice:
- 400 Bad Request for validation failures (MethodArgumentNotValidException, BindException, ConstraintViolationException, malformed JSON)
- 404 Not Found, 204 No Content, 503 Service Unavailable when applicable
- 500 Internal Server Error for unexpected errors

## Rate Limiting

A simple fixed-window rate limiter guards requests:
- Configuration (application properties with defaults):
  - ratelimit.enabled=true
  - ratelimit.window-ms=60000
  - ratelimit.max-requests=100

## Testing

- Run tests: mvn test
- The suite includes:
  - Controller tests (positive/negative, validation and error cases)
  - Service tests (business logic and edge cases)
  - Mapper tests (null/unknown handling and ordering)
  - Rule tests (all error messages and pass scenarios)
  - Repository tests (custom queries)
  - Rate limiter tests (windowing, concurrency, and toggling)

## Docker

- Build image:
  - mvn spring-boot:build-image -DskipTests
- Run container:
  - docker run -p 8080:8080 mortgage-service:1.0.0

## Notes

- Requires Java 21 GA only; no --enable-preview needed.
- Default server port is 8080.