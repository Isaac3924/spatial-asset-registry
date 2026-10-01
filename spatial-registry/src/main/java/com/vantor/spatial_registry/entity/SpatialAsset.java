package com.vantor.spatial_registry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

@Entity
@Table(name = "spatial_assets")
public class SpatialAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "assetId is required")
    @Column(nullable = false, unique = true)
    private String assetId;

    @NotBlank(message = "sensorType is required")
    @Column(nullable = false)
    private String sensorType;

    @NotNull(message = "latitude is required")
    @DecimalMin(value = "-90.0", message = "latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90.0")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "longitude is required")
    @DecimalMin(value = "-180.0", message = "longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180.0")
    @Column(nullable = false)
    private Double longitude;

    @DecimalMin(value = "0.0", message = "cloudCoverPercentage must be >= 0.0")
    @DecimalMax(value = "100.0", message = "cloudCoverPercentage must be <= 100.0")
    private Double cloudCoverPercentage;

    @NotNull(message = "capturedAt is required")
    @Column(nullable = false)
    private Instant capturedAt;

    @URL(message = "imageUrl must be a valid URL")
    private String imageUrl;

    public SpatialAsset() {
    }

    public SpatialAsset(String assetId, String sensorType, Double latitude, Double longitude,
            Double cloudCoverPercentage, Instant capturedAt, String imageUrl) {
        this.assetId = assetId;
        this.sensorType = sensorType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cloudCoverPercentage = cloudCoverPercentage;
        this.capturedAt = capturedAt;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getCloudCoverPercentage() {
        return cloudCoverPercentage;
    }

    public void setCloudCoverPercentage(Double cloudCoverPercentage) {
        this.cloudCoverPercentage = cloudCoverPercentage;
    }

    public Instant getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Instant capturedAt) {
        this.capturedAt = capturedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
