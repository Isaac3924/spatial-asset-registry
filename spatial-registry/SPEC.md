# Specification: Geo spatial Asset Registry API

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

## 4. Verification
- Provide JUnit 5 unit and integration tests using `@WebMvcTest` and `@DataJpaTest` to verify validation rules (especially latitude/longitude range bounds).