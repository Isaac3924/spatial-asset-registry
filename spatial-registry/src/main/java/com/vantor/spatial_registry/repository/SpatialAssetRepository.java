package com.vantor.spatial_registry.repository;

import com.vantor.spatial_registry.entity.SpatialAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpatialAssetRepository extends JpaRepository<SpatialAsset, Long> {

    Optional<SpatialAsset> findByAssetId(String assetId);

    List<SpatialAsset> findBySensorType(String sensorType);
}
