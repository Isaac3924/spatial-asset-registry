# Spatial Asset Registry API

A cloud-native Java/Spring Boot microservice for ingesting, validating, and querying geospatial asset metadata (STAC-inspired).

## Agentic SDLC & Spec-Driven Development
This project was built to demonstrate an **AI-Assisted (Agentic) Software Development Lifecycle**.
1. **Specification First:** The domain model, strict coordinate boundaries, HTTP contracts, and CI/CD targets were explicitly defined in [`SPEC.md`](./spatial-registry/SPEC.md) before implementation.
2. **AI Implementation:** GitHub Copilot Chat (Claude Sonnet 5) was utilized as an autonomous agent within VS Code to generate the boilerplate, apply Jakarta Bean Validation, write the JUnit 5 test suites, and craft the AWS deployment workflow.
3. **Engineering Validation:** AI outputs were strictly reviewed to ensure proper `@Valid` boundary enforcement (e.g., rejecting out-of-bounds latitude/longitude coordinates), clean N-Tier architectural separation, and secure cloud permissions.

## Tech Stack
* **Language:** Java 21 LTS
* **Framework:** Spring Boot 3.4
* **Persistence:** Spring Data JPA, H2 In-Memory Database
* **Containerization:** Docker (multi-stage, `linux/amd64`)
* **Cloud Infrastructure:** AWS ECS on Fargate, Amazon ECR
* **CI/CD:** GitHub Actions with OIDC (OpenID Connect) authentication
* **Testing:** JUnit 5, `@WebMvcTest`, `@DataJpaTest`

## API Endpoints
Base URL: `http://localhost:8080/api/v1/assets`

* `POST /`: Ingest a new spatial asset. Enforces strict coordinate validation (Lat: -90 to 90, Lon: -180 to 180).
* `GET /`: Retrieve all ingested assets.
* `GET /{assetId}`: Retrieve a specific asset by its catalog ID.
* `GET /search?sensorType={type}`: Filter assets by sensor payload (e.g., OPTICAL, SAR).
* `GET /bbox`: Geospatial bounding box filter (`minLat`, `maxLat`, `minLon`, `maxLon`).

## CI/CD Pipeline
Continuous integration and continuous deployment are automated via GitHub Actions:
- **Trigger:** Any push to the `main` branch.
- **Security:** Authenticates to AWS via OpenID Connect (OIDC) using short-lived tokens and repository secret `AWS_ROLE_ARN` (no static AWS access keys stored).
- **Automated Delivery:** Compiles and packages the Docker container, pushes to Amazon ECR, and initiates an immediate rolling deployment on Amazon ECS Fargate.

## Running Locally
Ensure you have JDK 21 installed, then navigate into the project directory and run the included Maven wrapper from the terminal:
```bash
cd spatial-registry
./mvnw spring-boot:run
```