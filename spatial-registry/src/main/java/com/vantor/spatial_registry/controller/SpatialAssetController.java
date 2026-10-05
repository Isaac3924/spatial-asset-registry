package com.vantor.spatial_registry.controller;

import com.vantor.spatial_registry.entity.SpatialAsset;
import com.vantor.spatial_registry.repository.SpatialAssetRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class SpatialAssetController {

    private final SpatialAssetRepository repository;

    public SpatialAssetController(SpatialAssetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<SpatialAsset> ingestAsset(@Valid @RequestBody SpatialAsset asset) {
        SpatialAsset saved = repository.save(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<SpatialAsset>> getAllAssets() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<SpatialAsset> getAssetByAssetId(@PathVariable String assetId) {
        return repository.findByAssetId(assetId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpatialAsset>> searchBySensorType(@RequestParam String sensorType) {
        return ResponseEntity.ok(repository.findBySensorType(sensorType));
    }

    @GetMapping("/bbox")
    public ResponseEntity<List<SpatialAsset>> searchByBoundingBox(@RequestParam Double minLat,
            @RequestParam Double maxLat, @RequestParam Double minLon, @RequestParam Double maxLon) {
        return ResponseEntity.ok(repository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLon, maxLon));
    }
}
