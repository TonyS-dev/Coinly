<div style="text-align: center;">

# Coinly — Finance Tracker Backend

**A modular Kotlin + Spring Boot backend for a personal finance tracker (REST API, JWT auth, Postgres, Flyway, Docker)**

</div>

<div style="text-align: center;">

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-blue?logo=kotlin&logoColor=white)](https://kotlinlang.org/) 
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.5.7-brightgreen?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot) 
[![Java](https://img.shields.io/badge/Java-17-purple?logo=java&logoColor=white)](https://adoptium.net/) 
[![Build: Gradle](https://img.shields.io/badge/Gradle-8.x-darkgreen?logo=gradle&logoColor=white)](https://gradle.org/) 
[![Docker](https://img.shields.io/badge/Docker-yes-blue?logo=docker&logoColor=white)](https://www.docker.com/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](./LICENSE)

</div>

---

## Overview 🚀

Coinly is a modular backend service written in Kotlin and Spring Boot that provides the API layer for a finance tracker application. The backend focuses on clean modular boundaries (user, transaction, currency converter modules), JWT-based authentication, Postgres persistence with Flyway migrations, caching, resilience, and observability.

Primary goals:
- Provide a secure REST API for client apps (Android / Web)
- Keep modules decoupled using ports & adapters patterns
- Offer a reproducible dev environment via Docker

---

## Tech Stack & Key Dependencies 🛠️

- Language: Kotlin 1.9.25
- JVM: Java 17 (toolchain)
- Framework: Spring Boot 3.5.7
- Build: Gradle (Kotlin DSL)
- Persistence: PostgreSQL (runtime), Spring Data JPA, Hibernate
- Migrations: Flyway
- Security: Spring Security + JWT (jjwt 0.11.5)
- Observability: Spring Boot Actuator + Micrometer (Prometheus)
- Resilience: Spring Cloud Circuit Breaker (Resilience4j)
- Caching: Spring Cache + Caffeine
- OpenAPI: springdoc-openapi-starter-webmvc-ui
- Mapping: MapStruct
- Testing: Spring Boot Starter Test, JUnit Platform, MockK, spring-security-test
- Containers: Docker & docker-compose

Files inspected for versions: `build.gradle.kts`, `Dockerfile`, `docker-compose.yml`.

---

## Project structure (main directories) 📁

A focused view of the core layout (important files/paths):

```
build.gradle.kts
settings.gradle.kts
Dockerfile
docker-compose.yml
src/main/kotlin/com/coinly/
├── CoinlyApplication.kt            # Spring Boot entrypoint
├── common/                         # shared utilities, exceptions, DTOs
├── security/                       # JWT provider, filters, security config
├── user/                           # user module (domain, infra, controller)
├── transaction/                    # transactions module
├── currency_converter/             # external API client and adapter
src/main/resources/
├── application.yml                 # configuration for profiles (local/docker/prod)
└── db/migration/                    # Flyway migration SQL scripts (V1, V2, V3...)
Dockerfile
docker-compose.yml
README_guide.md
HELP.md
```

Important paths referenced in the repo:
- `src/main/resources/application.yml` — main configuration (profiles: local, docker, prod)
- `Dockerfile` — multi-stage build for packaging
- `docker-compose.yml` — postgres + app composition

---

## Getting started (developer friendly) 👩‍💻

Prerequisites:
- Java 17 (or use Gradle toolchain)
- Gradle (wrapper included) — use `./gradlew`
- Docker & docker-compose (if you want containerized DB/service)
- PostgreSQL (local) or use the docker-compose DB

Minimum versions:
- Java 17
- Gradle 8.x (wrapper used in repo)
- Kotlin 1.9.x (declared in build.gradle.kts)

### 1) Clone

```bash
git clone https://github.com/TonyS-dev/coinly.git
cd coinly
```

### 2) Environment variables

Create a `.env` file or export environment variables used by `application.yml` and `docker-compose`:

Required variables (development / docker):
- DB_NAME — e.g. coinly_dev
- DB_USER — e.g. coinly
- DB_PASSWORD — e.g. secret
- JWT_SECRET — a secure secret for signing JWTs
- JWT_EXPIRATION — expiration period in ms or seconds (project expects an env var)
- (optional) DB_PORT — local DB port (default 5432)

Example `.env` (DO NOT commit secrets):

```env
DB_NAME=coinly_dev
DB_USER=coinly
DB_PASSWORD=secret
JWT_SECRET=changeme_dev_secret
JWT_EXPIRATION=86400000
```

### 3) Run with local DB (Postgres installed locally)

Start Postgres and ensure the DB credentials match `.env` or the environment. Then run:

```bash
# build and run via Gradle
./gradlew bootJar
java -jar build/libs/*.jar
```

Or run directly in dev mode:

```bash
./gradlew bootRun
```

### 4) Run with Docker (recommended for parity)

Build + run with docker-compose (reads env vars from your shell or a `.env` file):

```bash
# build the app and start services
docker compose up --build
# or in older setups
# docker-compose up --build
```

The `docker-compose.yml` starts:
- `postgres` (Postgres 15-alpine)
- `app` (coinly backend)

Networking note: the `docker` profile uses `jdbc:postgresql://postgres:5432/${DB_NAME}` in `application.yml` so the service name `postgres` is used inside Docker.

### 5) Run tests

```bash
# run unit + integration tests
./gradlew test
```

---

## Configuration & Environment 🔐

Primary config file: `src/main/resources/application.yml` with multiple profiles: `local`, `docker`, `prod`.

Key properties (examples):

- `server.port` — default 8080
- `spring.datasource.*` — configured per-profile (local/docker/prod)
- `jwt.secret` — must be provided via env var `JWT_SECRET`
- `jwt.expiration` — `JWT_EXPIRATION`
- `external.exchange-rate-api.url` — base URL for currency conversion

Profiles behavior:
- `local` — expects local Postgres (jdbc:postgresql://localhost:${DB_PORT:5432}/${DB_NAME})
- `docker` — expects DB at `postgres:5432` (service name in docker-compose)
- `prod` — expects full `DATABASE_URL` and tuned Hikari pool settings

---

## Features ✅

- Modular monolith with modules for Users, Transactions and Currency Converter
- JWT authentication and role-based authorization
- Postgres persistence with Flyway migrations (see `src/main/resources/db/migration`)
- Caching (Caffeine) for expensive external calls
- External currency rate integration with resilience (circuit breaker + fallback)
- Observability with Actuator + Prometheus
- OpenAPI / Swagger UI for API exploration
- Dockerized development environment (Dockerfile + docker-compose)

---

## API & Documentation 📚

OpenAPI (springdoc) is included. When the app is running locally, the Swagger UI is usually available at:

```
http://localhost:8080/swagger-ui/index.html
# or
http://localhost:8080/swagger-ui.html
```

API base path convention: many controllers expose endpoints under `/api/**`. Check the actual controllers under `src/main/kotlin/com/coinly/*/infrastructure/controller/` to see exact routes.

Example calls (replace host/port and tokens with your values):

```bash
# public or general GET (example)
curl -v http://localhost:8080/api/transactions

# auth - login (example)
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"you@example.com","password":"secret"}'

# protected - use Authorization header with Bearer token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/transactions
```

Note: Exact endpoints / payloads will depend on controllers defined in the modules (user/auth controllers, transaction controllers). Use the running OpenAPI UI to explore the full contract.

---

## Observability & Admin

- Actuator endpoints: `/actuator/*` (liveness, metrics, health)
- Prometheus metrics available when micrometer + prometheus registry is enabled

---

## CI / Deployment 🛫

Simple Docker build & run flow (CI can use these steps):

```bash
# build jar locally
./gradlew clean build -x test

# build docker image (if needed)
docker build -t coinly-backend:local .

# run compose
docker compose up -d --build
```

CI notes:
- Tests should run in CI before building images.
- Use secrets manager to inject `JWT_SECRET` and DB credentials in production.
- `Dockerfile` uses a multi-stage Gradle build; GitHub Actions or similar can reuse that.

---

## Contributing 🤝

Small guide to contribute:
- Follow existing package/module structure (domain, repository port, service/use case, infra adapters, controllers)
- Keep public-facing APIs backward compatible when possible
- Add unit tests for domain logic and integration tests for DB adapters
- Run `./gradlew test` before opening PR
- Use descriptive commits and reference relevant issue numbers

---

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0).
See the full text in the `LICENSE` file at the repository root: `./LICENSE`.

The GNU GPLv3 is a copyleft license that requires derived works to be
released under the same license. If you plan to embed or distribute this
project within other software, please review the terms of GPLv3 carefully.

---

## Author / Contact ✉️

- Antonio Santiago (TonyS-dev)
- GitHub: https://github.com/TonyS-dev
- Email: santiagor.acarlos@gmail.com

---
