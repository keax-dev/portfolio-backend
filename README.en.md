<p align="right">
  <a href="./README.md">
    <img src="https://img.shields.io/badge/README-Espa%C3%B1ol-1f6feb?style=for-the-badge" alt="README en Español" />
  </a>
  <a href="./README.en.md">
    <img src="https://img.shields.io/badge/README-English-0e8a16?style=for-the-badge" alt="README in English" />
  </a>
</p>

# Portfolio Backend

Backend API for the public portfolio and admin panel, built with Spring Boot 3.5, Java 21, hexagonal architecture, PostgreSQL, Flyway, OpenAPI/Swagger, Actuator, Docker, and automated GitHub Actions deployment.

## Functional overview

This backend exposes two capability groups:

- Public endpoints used to render the portfolio, register visitors, and send contact messages.
- JWT-protected admin endpoints used to manage profile, education, skills, technologies, projects, institutions, social links, and images.

Main routes:

- `/api/auth/login`: admin authentication.
- `/api/portfolio/*`: public portfolio consumption.
- `/api/profile`, `/api/education`, `/api/skill`, `/api/technology`, `/api/project`, `/api/institution`, `/api/socialNetwork`: admin CRUD.
- `/api/image/*`: image upload endpoints.
- `/api/visitor` and `/api/visitor/dashboard`: visitor registration and reporting.

## What is implemented

### Authentication and security API

- JWT-based login for the admin panel.
- Token authentication filter.
- HTTP security and headers configuration.
- Protected admin endpoints with separation between public and private routes.

### Public portfolio API

- Main profile retrieval.
- Education, skills, technologies, and social links retrieval.
- Contact form submission.
- Visitor registration with time-window deduplication.
- Client IP resolution with controlled proxy-header support.

### Administrative API

- Profile CRUD.
- Education CRUD.
- Institution CRUD.
- Skill CRUD.
- Technology CRUD.
- Project CRUD.
- Social network CRUD.
- Image upload to Cloudinary.
- Visitor dashboard.

### Infrastructure and observability

- Versioned database migrations with Flyway.
- OpenAPI documentation and Swagger UI.
- Healthchecks and metrics through Spring Boot Actuator.
- Prometheus export.
- Request correlation ID support.
- Global exception handling.
- Environment-driven logging configuration.

## Project architecture

The project is organized with a hexagonal architecture approach:

- `application`: use cases
- `domain`: models and ports
- `infrastructure`: web controllers, persistence adapters, security, mail, storage, and configuration
- `shared`: cross-cutting exceptions, filters, and configuration

Each functional module follows the same idea. For example:

- `profile`, `education`, `project`, `technology`, `skill`, `institution`, `socialnetwork`, `visitor`, `auth`, `email`, `uploadimage`

This helps:

- Isolate business logic
- Test use cases without Spring MVC dependencies
- Replace adapters without breaking the domain
- Validate architectural boundaries with ArchUnit.

## Technical stack

- Java 21
- Spring Boot 3.5.15
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Mail
- Thymeleaf
- PostgreSQL
- Flyway
- springdoc OpenAPI + Swagger UI
- Spring Boot Actuator
- Micrometer Prometheus
- Cloudinary
- JWT (`jjwt`)
- JUnit 5
- Mockito
- H2
- Testcontainers
- ArchUnit
- Docker
- GitHub Actions

## Profiles and environment configuration

The application does not define a default Spring profile. This was intentional to avoid accidentally loading development values in production.

Available profiles:

- `dev`: imports `./.env.dev`
- `prod`: imports `./.env`

Relevant files:

- `src/main/resources/application.properties`: base configuration expressed through environment variables.
- `src/main/resources/application-dev.properties`: imports `.env.dev`.
- `src/main/resources/application-prod.properties`: imports `.env`.
- `.env.example`: reference template for real environment files.
- `.env.dev`: local development values.
- `.env`: production values when the backend runs with the `prod` profile.

## Requirements

For local development:

- Java 21
- Locally available PostgreSQL
- Required variables configured in `.env.dev`

For validation and builds:

- Maven Wrapper (`mvnw`) already included in the repository

## Local run

### From PowerShell

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

### From IntelliJ IDEA

In the Spring Boot run configuration:

- `Active profiles`: `dev`

This makes Spring load `application-dev.properties`, which then imports `.env.dev`.

### Local build

```powershell
.\mvnw.cmd clean package
```

## Environment variables

The backend uses environment variables for:

- HTTP server settings
- Datasource and Hikari pool
- Hibernate and Flyway
- CORS
- Logging
- Actuator and Prometheus
- OpenAPI/Swagger
- Cloudinary
- SMTP mail
- JWT
- Contact rate limiting
- Visitor deduplication.

The base template lives in:

- `.env.example`

## Database and migrations

Flyway is the official schema versioning mechanism.

Current migrations:

- `src/main/resources/db/migration/V1__init_schema.sql`

Relevant configuration:

- `spring.flyway.enabled=true`
- `spring.flyway.baseline-on-migrate=true`

This allows you to:

- Bootstrap new environments consistently
- Version schema changes together with code
- Adopt Flyway on an existing database without losing data on the first migration if the environment already existed.

## Documentation and observability

### Swagger UI

With the application running:

- `http://localhost:8080/swagger-ui.html`

### OpenAPI JSON

- `http://localhost:8080/v3/api-docs`

### Actuator

- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/metrics`
- `http://localhost:8080/actuator/prometheus`

### What these endpoints are used for

- `health`: local, Docker, Nginx, or load balancer healthchecks
- `metrics` and `prometheus`: monitoring and scraping
- `swagger-ui`: easy API exploration and testing without Postman

## Testing and quality

The main backend validation command is:

```powershell
.\mvnw.cmd clean verify
```

That command covers:

- Compilation
- Unit tests
- Integration tests
- Architecture tests
- JaCoCo reporting
- Minimum coverage enforcement.

### Test types already present

- Use case tests: `*UseCasesTest`, `*UseCaseImplTest`
- Security tests: `SecurityConfigTest`, `SecurityHeadersTest`, `JwtAuthenticationFilterTest`
- Web/controller integration tests: `*ApiIntegrationTest`
- Persistence tests: `Jpa*RepositoryIntegrationTest`, `PostgreSqlPersistenceIntegrationTest`
- Architecture tests: `HexagonalArchitectureTest`
- Mapper and contract tests: `MapperContractsTest`
- Backend end-to-end test: `BackendEndToEndTest`
- Observability and documentation tests: `ActuatorApiIntegrationTest`, `ManagementApiIntegrationTest`, `OpenApiIntegrationTest`

### Coverage

The build currently enforces:

- Minimum `75%` line coverage
- Minimum `50%` branch coverage

Generated report:

- `target/site/jacoco/index.html`

Maven Enforcer also validates:

- Java 21
- Maven 3.9+

## Docker

The backend includes a multi-stage `Dockerfile`:

- Stage 1: build with Maven + Temurin 21
- Stage 2: run with a lightweight JRE image

Image characteristics:

- Installs `curl` for internal healthchecks
- Exposes `8080`
- Allows `JAVA_OPTS` injection
- Uses `/actuator/health` as the container healthcheck.

Example local build:

```powershell
docker build -t portfolio-backend:local .
```

## Production deployment with Docker

Production deployment is designed for Lightsail using Docker Compose.

Main file:

- `docker-compose.prod.yml`

Current deployment model:

- The image is published to GHCR
- The server pulls the exact image tag `sha-*`
- The container uses `network_mode: host`
- The backend is available on `127.0.0.1:8080`
- Nginx works as the public reverse proxy.

Expected deployment directory:

- `/opt/portfolio-backend`

Expected files there:

- `.env`: real backend variables
- `.env.prod`: image reference generated by the workflow
- `docker-compose.prod.yml`

### Example server command

```bash
cd /opt/portfolio-backend
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d
```

## CI/CD

The project already includes a full GitHub Actions automation flow.

### 1. CI

File:

- `.github/workflows/ci.yml`

What it does:

- Runs on `push` to `main`
- Runs on `pull_request` targeting `main`
- Executes `./mvnw -B verify`
- Validates that the backend Docker image can be built
- Uploads test artifacts if something fails

### 2. Deploy Prod

File:

- `.github/workflows/deploy-prod.yml`

What it does:

- Triggers when `CI` finishes successfully on `main`
- Publishes the image to GHCR
- Tags the image as `latest` and `sha-<commit>`
- Deploys on Lightsail through a self-hosted runner
- Validates startup with `curl http://127.0.0.1:8080/actuator/health`

### 3. Rollback Prod

File:

- `.github/workflows/rollback-prod.yml`

What it does:

- Runs manually
- Receives an `image_tag`
- Redeploys a previously published GHCR image
- Validates the healthcheck again after rollback

## Required GitHub configuration

### Environment

- `production`

### Deployment secrets

- `LIGHTSAIL_BACKEND_PATH`
- `GHCR_READ_TOKEN`

### Expected self-hosted runner

Labels:

- `self-hosted`
- `Linux`
- `X64`
- `production`

## Simplified structure

```text
src/
  main/
    java/com/keax/
      auth/
      education/
      email/
      institution/
      portfolio/
      profile/
      project/
      shared/
      skill/
      socialnetwork/
      technology/
      uploadimage/
      visitor/
    resources/
      application.properties
      application-dev.properties
      application-prod.properties
      db/migration/
  test/
    java/com/keax/
      architecture/
      e2e/
      management/
      persistence/
      ...
```

## Current backend status

The project already includes:

- Hexagonal architecture verified by tests
- JWT security
- Swagger documentation
- Actuator and Prometheus observability
- Flyway migrations
- Mandatory coverage thresholds
- Multi-stage Docker image
- CI pipeline
- Automated production deployment
- Manual rollback by image tag.

## Useful commands

### Full verification

```powershell
.\mvnw.cmd clean verify
```

### Run locally with development profile

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

### Build Docker image

```powershell
docker build -t portfolio-backend:local .
```
