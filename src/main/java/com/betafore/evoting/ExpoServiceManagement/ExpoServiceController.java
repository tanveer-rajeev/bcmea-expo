package com.betafore.evoting.ExpoServiceManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expoServices")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class ExpoServiceController {

    private final ExpoServiceDaoImpl expoServiceDaoIml;

    @Operation(summary = "Create expo-service")
    @PostMapping
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody ExpoService expoService) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(expoServiceDaoIml.save(expoService)).build());
    }

    @Operation(summary = "Save all expo-service")
    @PostMapping("/saveAll")
    public ResponseEntity<ApiResponse> saveAll(@Valid @RequestBody ExpoServiceDto expoServiceDto) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(expoServiceDaoIml.saveAll(expoServiceDto)).build());
    }

    @Operation(summary = "Get expo-service")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getExpoServiceById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(expoServiceDaoIml.findById(id)).build());
    }

    @Operation(summary = "Get all expo-service")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExpoServiceByIds() {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(expoServiceDaoIml.all()).build());
    }

    @Operation(summary = "Update expo-service")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateExpoService(@RequestBody ExpoService expoService, @PathVariable Long id) throws CustomException {
        expoServiceDaoIml.saveAndFlush(expoService, id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Delete expo-service")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExpoServiceById(@PathVariable Long id) throws CustomException {
        expoServiceDaoIml.removeById(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all expo-service")
    @DeleteMapping
    public  ResponseEntity<ApiResponse> deleteAllExpoService() {
        expoServiceDaoIml.removeAll();
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }
}
