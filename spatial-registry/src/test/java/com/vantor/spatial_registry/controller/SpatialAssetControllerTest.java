package com.vantor.spatial_registry.controller;

import com.vantor.spatial_registry.entity.SpatialAsset;
import com.vantor.spatial_registry.repository.SpatialAssetRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpatialAssetController.class)
class SpatialAssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SpatialAssetRepository repository;

    private SpatialAsset validAsset() {
        return new SpatialAsset("LANDSAT-09-2026-A1", "OPTICAL", 45.0, 90.0, 12.5,
                Instant.parse("2026-01-01T00:00:00Z"), "https://example.com/image.png");
    }

    @Test
    void ingestAsset_withValidPayload_returns201() throws Exception {
        SpatialAsset saved = validAsset();
        saved.setId(1L);
        when(repository.save(any(SpatialAsset.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAsset())))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(doubles = { 90.1, -90.1 })
    void ingestAsset_withOutOfRangeLatitude_returns400(double latitude) throws Exception {
        SpatialAsset asset = validAsset();
        asset.setLatitude(latitude);

        mockMvc.perform(post("/api/v1/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = { 180.1, -180.1 })
    void ingestAsset_withOutOfRangeLongitude_returns400(double longitude) throws Exception {
        SpatialAsset asset = validAsset();
        asset.setLongitude(longitude);

        mockMvc.perform(post("/api/v1/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAssetByAssetId_whenMissing_returns404() throws Exception {
        when(repository.findByAssetId("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/assets/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAssetByAssetId_whenPresent_returns200() throws Exception {
        when(repository.findByAssetId("LANDSAT-09-2026-A1")).thenReturn(Optional.of(validAsset()));

        mockMvc.perform(get("/api/v1/assets/LANDSAT-09-2026-A1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchBySensorType_returns200() throws Exception {
        when(repository.findBySensorType("OPTICAL")).thenReturn(List.of(validAsset()));

        mockMvc.perform(get("/api/v1/assets/search").param("sensorType", "OPTICAL"))
                .andExpect(status().isOk());
    }
}
