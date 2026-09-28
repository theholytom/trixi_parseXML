# trixi_parseXML

Spring Boot application for downloading a ZIP with Czech municipality XML data, parsing it, and storing normalized municipality + municipality-part records in PostgreSQL.

## What this app does

- Triggers an asynchronous parsing run that downloads XML data from a configurable remote URL.
- Parses municipality (`Obec`) and municipality part (`CastObce`) information from the XML source.
- Stores parsed data in PostgreSQL (create/update behavior by municipality code).
- Exposes REST endpoints for:
  - triggering and checking parsing runs,
  - listing/fetching municipalities and municipality parts,
  - reading/updating source URL configuration.
- Writes structured logs to file (`logs/parsexml.log`) with append behavior.

## Implemented features

- Asynchronous parsing job execution.
- Parsing run tracking with status and timestamps.
- REST API with pageable list endpoints.
- Source URL runtime update endpoint.
- Dockerized app + PostgreSQL via Docker Compose.
- File-based rolling logging (console + file).

## Technology stack

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA (Hibernate)
- PostgreSQL 16
- Maven Wrapper (`./mvnw`)
- Docker / Docker Compose
- SLF4J + Logback
- Lombok

## Project structure

```text
src/main/java/cz/trixi/parsexml
├── api/            # REST controllers + DTOs
├── config/         # Async and client configuration properties
├── job/            # Parsing orchestration and services
├── persistence/
│   ├── entity/     # JPA entities
│   └── repository/ # Spring Data repositories
└── ParsexmlApplication.java

src/main/resources
├── application.properties
└── logback-spring.xml
```

## API endpoints

Base path: `/api/v1`

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/v1/tasks/run` | Trigger a new async parsing run |
| GET | `/api/v1/tasks/{id}` | Get parsing run detail by run ID |
| GET | `/api/v1/tasks` | List parsing runs (optional `status`, pageable) |
| GET | `/api/v1/municipalities` | List municipalities (pageable) |
| GET | `/api/v1/municipalities/{id}` | Get municipality by ID |
| GET | `/api/v1/municipality_parts` | List municipality parts (pageable) |
| GET | `/api/v1/municipality_parts/{id}` | Get municipality part by ID |
| GET | `/api/v1/config/url` | Get current XML source URL |
| POST | `/api/v1/config/url` | Update XML source URL |

## Requirements

- Docker Engine with Docker Compose support

## Run with Docker Compose

1. Build and start:

```bash
docker compose up --build
```

2. Stop services:

```bash
docker compose down
```

## Runtime notes

- App is exposed on: `http://localhost:8080`
- PostgreSQL is exposed on host: `localhost:5433`
- Inside Docker network, app connects to DB using service name `postgres:5432`.
- Logs are persisted to project folder `./logs` via compose volume mapping:

```yaml
volumes:
  - ./logs:/app/logs
```