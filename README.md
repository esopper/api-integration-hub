# API Integration Hub (Backend) — WIP

A portfolio project demonstrating how I design and build an integrations-focused backend: clean API boundaries, upstream HTTP clients, DTO mapping, and test coverage.

> **Status:** Work in progress. Expect breaking changes while I iterate on structure, endpoints, and the overall product direction.

## What it does

The backend exposes a simple REST API that aggregates/normalizes data from external services and returns a consistent response shape to a frontend.

Currently implemented / in-flight integrations:
- **GitHub**: repository search (via GitHub REST API)
- **OpenWeather**: weather lookup (via OpenWeather API)

## Tech stack

- **Java** (Spring Boot)
- **Gradle**
- **HTTP clients**: internal client layer (`*HttpClient`) for upstream calls
- **DTO mapping** (mapper layer)
- **Testing**: JUnit, integration-style tests (WireMock support)

## Project structure (high level)

- `web/controller` — REST controllers and request/response contracts
- `web/dto` — API DTOs returned to clients
- `service` — orchestration/business logic
- `integrations/*` — upstream API clients, upstream DTOs, and integration exceptions
- `mapper` — translation between upstream DTOs and API DTOs

## Running locally

### Prerequisites
- Java 17+ (or the version defined in the project)
- Gradle (wrapper included)

### Configure secrets (local profile)

This project uses a Spring `local` profile for development secrets.

1. Create the following file (this file is **not committed** to Git):

`src/main/resources/application-local.properties`

2. Add your API credentials:

```properties
github.token=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
openweather.api.key=YOUR_OPENWEATHER_API_KEY
```

3. Run the application with the `local` profile active:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

> **Note:** `application-local.properties` is gitignored and should never be committed.

