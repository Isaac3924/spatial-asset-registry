package com.vantor.spatial_registry.repository;

import com.vantor.spatial_registry.entity.SpatialAsset;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
class SpatialAssetRepositoryTest {

    @Autowired
    private SpatialAssetRepository repository;

    private SpatialAsset validAsset() {
        return new SpatialAsset("LANDSAT-09-2026-A1", "OPTICAL", 45.0, 90.0, 12.5,
                Instant.parse("2026-01-01T00:00:00Z"), "https://example.com/image.png");
    }

    @Test
    void save_withValidAsset_persistsSuccessfully() {
        SpatialAsset saved = repository.saveAndFlush(validAsset());

        assertThat(saved.getId()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(doubles = { 90.1, -90.1 })
    void save_withOutOfRangeLatitude_throwsConstraintViolation(double latitude) {
        SpatialAsset asset = validAsset();
        asset.setLatitude(latitude);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.saveAndFlush(asset));
    }

    @ParameterizedTest
    @ValueSource(doubles = { 180.1, -180.1 })
    void save_withOutOfRangeLongitude_throwsConstraintViolation(double longitude) {
        SpatialAsset asset = validAsset();
        asset.setLongitude(longitude);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.saveAndFlush(asset));
    }

    @Test
    void findBySensorType_returnsMatchingAssets() {
        repository.saveAndFlush(validAsset());

        List<SpatialAsset> results = repository.findBySensorType("OPTICAL");

        assertThat(results).hasSize(1);
    }

    @Test
    void findByAssetId_whenPresent_returnsAsset() {
        repository.saveAndFlush(validAsset());

        Optional<SpatialAsset> found = repository.findByAssetId("LANDSAT-09-2026-A1");

        assertThat(found).isPresent();
    }
}
