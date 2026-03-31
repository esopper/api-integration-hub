# API Integration Hub (Backend)

A portfolio project demonstrating how I design and build an integrations-focused backend: clean API boundaries, upstream HTTP clients, DTO mapping, testing, and cloud deployment.

## Overview

API Integration Hub is the backend portion of my portfolio application hosted at **eriksopper.com**. It is designed to showcase how I structure a Spring Boot application that integrates with third-party APIs and exposes a consistent REST API to a frontend.

This project emphasizes:

- clean separation between web, service, mapper, and integration layers
- explicit upstream HTTP client abstractions
- DTO-based API boundaries
- environment-based configuration for secrets and deployment
- test coverage including integration-style tests

## Live Demo & Architecture

- **Frontend (AWS Amplify)**  
  https://www.eriksopper.com

- **Backend API (AWS Elastic Beanstalk + ALB + HTTPS)**  
  https://api.eriksopper.com

### Architecture overview

- Angular frontend hosted on **AWS Amplify**
- Spring Boot backend deployed via **AWS Elastic Beanstalk**
- **Application Load Balancer (ALB)** handles incoming traffic
- **TLS termination at the ALB** using AWS Certificate Manager (ACM)
- Backend instances run behind the ALB over HTTP
- Environment variables used for configuration and secrets

This setup demonstrates a production-style separation of concerns between frontend hosting and backend infrastructure, along with secure HTTPS handling and cloud-managed deployment.

## Current integrations

- **GitHub**: paginated repository search via the GitHub REST API
- **OpenWeather**: weather lookup via the OpenWeather API

## Tech stack

- **Java 17**
- **Spring Boot**
- **Gradle**
- **JUnit**
- **WireMock** for integration-style testing

## Project structure

    src/main/java/com/eriksopper/hub
    ├── config/                  # application configuration (CORS, HTTP clients, etc.)
    ├── integrations/            # upstream API clients, external DTOs, integration exceptions
    │   ├── github/
    │   └── openweather/
    ├── mapper/                  # mapping between upstream models and API DTOs
    ├── service/                 # orchestration and business logic
    ├── web/
    │   ├── controller/          # REST controllers
    │   ├── dto/                 # request/response DTOs exposed by this API
    │   └── exception/           # global exception handling
    └── domain/exception/        # shared domain-level exceptions

## Configuration

This application is configured primarily through **environment variables**.

In `application.properties`, sensitive values are read from environment variables rather than committed config files.

### Required environment variables

- `GITHUB_TOKEN`
- `OPENWEATHER_API_KEY`

### Optional environment variables

These may vary depending on your local or deployed setup:

- `PORT`
- `CORS_ALLOWED_ORIGINS`

## Spring profiles

Spring profiles are still available for environment-specific behavior, but **secrets are not stored in profile-specific files**.

That means:

- profiles can be used for behavior differences between local/test/deployed environments
- secrets should be injected through environment variables
- local development does **not** require committing or maintaining a secrets file such as `application-local.properties`

The project also includes a dedicated `integrationTest` source set and integration test resources for test-specific configuration.

## Running locally

### Prerequisites

- Java 17+
- Gradle wrapper included in the repo

### 1. Set environment variables

Example:

    export GITHUB_TOKEN=your_github_personal_access_token
    export OPENWEATHER_API_KEY=your_openweather_api_key

### 2. Start the application

    ./gradlew bootRun

By default, the app will use values from your environment.

### 3. Run tests

Unit and integration-style tests are both wired into the Gradle build.

    ./gradlew test
    ./gradlew integrationTest
    ./gradlew check

## Deployment

This backend is intended to be deployed as part of the portfolio site at **eriksopper.com**.

### AWS Elastic Beanstalk

The backend is hosted on **AWS Elastic Beanstalk**. I chose Elastic Beanstalk for this stage of the project because it lets me demonstrate:

- packaging and deploying a Java web application to AWS
- environment-based configuration and secret injection
- managed infrastructure for a production-style portfolio deployment
- a path to future enhancements such as additional AWS services and database-backed features

This complements the frontend, which is hosted separately on **AWS Amplify**.

## Why this project exists

This project is less about building a giant product and more about demonstrating how I think as an engineer:

- designing maintainable API boundaries
- integrating external systems cleanly
- organizing code for growth
- handling configuration in a deployment-friendly way
- building something real, hosted, and extensible as a portfolio piece

Over time, I plan to expand this application with additional integrations and features to demonstrate a broader range of backend and cloud skills.

## License

MIT