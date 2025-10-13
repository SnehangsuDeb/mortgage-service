# Mortgage Service - 1.0.0

A Spring Boot (3.4.x, Java 21) REST API that:
- Exposes mortgage interest rates
- Evaluates mortgage eligibility and calculates a monthly payment
- Uses H2 in-memory DB for persistence and seeding
- Generates OpenAPI-based controllers/models
- Enforces global rate limiting and consistent error handling
- Provides unit tests with JaCoCo code coverage
- Ships with a Dockerfile and optional remote debugging profile

## Tech Stack

- Java 21, Spring Boot 3.4.x (Web, Data JPA, Validation, Actuator)
- H2 in-memory database
- springdoc-openapi for Swagger UI
- OpenAPI Generator (server code stubs)
- Lombok
- Maven (jacoco-maven-plugin, surefire)
- Docker (multi-stage build)

## Getting Started

Prerequisites:
- JDK 21
- Maven 3.9+
- Docker (optional)

## Build:
- mvn clean install
- mvn clean package
- mvn spring-boot:run