# Specification: Geospatial Asset Registry API

## 1. Domain Model
Create an entity `SpatialAsset` with the following attributes:
- `id` (Long, auto-generated primary key)
- `assetId` (String, unique, non-null, e.g., "LANDSAT-09-2026-A1")
- `sensorType` (String, non-null, e.g., "OPTICAL", "SAR", "INFRARED")
- `latitude` (Double, non-null, valid range: -90.0 to 90.0)
- `longitude` (Double, non-null, valid range: -180.0 to 180.0)
- `cloudCoverPercentage` (Double, valid range: 0.0 to 100.0)
- `capturedAt` (Instant or LocalDateTime, non-null)
- `imageUrl` (String, valid URL format)

## 2. Persistence Layer
- Use Spring Data JPA repository (`SpatialAssetRepository`).
- Add a custom query method: `findBySensorType(String sensorType)`.
- Add a bounding box query method: `findByLatitudeBetweenAndLongitudeBetween(Double minLat, Double maxLat, Double minLon, Double maxLon)`.
- Use an in-memory H2 database.

## 3. API Layer (`SpatialAssetController`)
Base path: `/api/v1/assets`

Endpoints:
- `POST /api/v1/assets`: Ingest a new asset.
  - Request body validated with Bean Validation (`@Valid`).
  - Returns `201 Created` with the saved entity, or `400 Bad Request` with structured error messages if validation fails.
- `GET /api/v1/assets`: Retrieve all assets (returns `200 OK`).
- `GET /api/v1/assets/{assetId}`: Retrieve an asset by `assetId` (returns `200 OK` or `404 Not Found`).
- `GET /api/v1/assets/search?sensorType=...`: Filter assets by sensor type.
- `GET /api/v1/assets/bbox`: Filter assets within a geospatial bounding box.
  - Query parameters: `minLat`, `maxLat`, `minLon`, `maxLon` (all Double, required).
  - Returns `200 OK` with a list of matching assets.

## 4. Verification
- Provide JUnit 5 unit and integration tests using `@WebMvcTest` and `@DataJpaTest` to verify validation rules (especially latitude/longitude range bounds).

## 5. Continuous Integration & Deployment (CI/CD)
- **Platform:** GitHub Actions.
- **Trigger:** Push to the `main` branch.
- **Authentication:** Use `aws-actions/configure-aws-credentials` with OIDC (role-to-assume provided via GitHub Secrets as `AWS_ROLE_ARN`). Region is `us-east-1`.
- **Build & Push:** Authenticate via `amazon-ecr-login`, build the Docker image explicitly for `linux/amd64`, and push it to the ECR repository (`spatial-registry-api`).
- **Deploy:** Run `aws ecs update-service` with `--force-new-deployment` for the `spatial-registry-cluster` and `spatial-registry-service`.