# Spatial Asset Registry API

A cloud-native Java/Spring Boot microservice for ingesting, validating, and querying geospatial asset metadata (STAC-inspired).

## Agentic SDLC & Spec-Driven Development
This project was built to demonstrate an **AI-Assisted (Agentic) Software Development Lifecycle**.
1. **Specification First:** The domain model, strict coordinate boundaries, and HTTP contracts were explicitly defined in `SPEC.md` before implementation.
2. **AI Implementation:** GitHub Copliot Chat (Claude Sonnet 5) was utilized aas an autonomous agent within VS Code to generate the boilerplate, apply Jkarta Bean Validation, and write the JUnit 5 test suites.
3. **Engineering Validation:** AI outputs were strictly reviewed to ensure proper `@Valid` boundary enforcement (e.g., rejecting out-of-bounds latitude/longitude coordinates) and correct N-Tier architectural separation.

##Tech Stack
* **Language:** Java 21 LTS
* **Framework:** Spring Boot 3.4
* **Persistence:** Spring Data JPA, H2 In-Memory Database
* **Build Tool:** Maven Wrapper (`mvnw`)
* **Testing:** JUnit5, `@WebMvcTest`, `@DataJpaTest`

## API Endpoints
Base URL: `http://localhost:8080/api/v1/assets`

* `POST /`: Ingest a new spatial asset. Enforces strict coordinate validation (Lat: -90 to 90, Lon: -180 to 180).
* `GET /`: Retrieve all ingested assets.
* `GET /{assetId}`: Retrieve a specific asset by its catalog ID.
* `GET /search?sensorType={type}`: Filter assets by sensor payload (e.g., OPTICAL, SAR).

## Running Locally
Ensure you have JDK 21 installed, then run the included Maven wrapper from the terminal:
```bash
./mvnw spring-boot:run