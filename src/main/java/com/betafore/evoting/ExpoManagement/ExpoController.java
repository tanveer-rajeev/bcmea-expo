package com.betafore.evoting.ExpoManagement;

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
@RequestMapping("/api/v1/expo")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class ExpoController {

    private final ExpoDaoImpl expoDaoIml;

    @GetMapping("/getAuthLink/{expoId}")
    public ResponseEntity<ApiResponse> createExpoAuthLink(@PathVariable Long expoId) {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .message(expoDaoIml.createExpoAuthenticationUri(expoId))
                .build());
    }

    @Operation(summary = "Create Expo")
    @PostMapping
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody ExpoDto expo) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(expoDaoIml.save(expo)).build());
    }

    @Operation(summary = "Get Expo by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getExpoById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(expoDaoIml.findById(id)).build());
    }

    @Operation(summary = "Get all expo services that attached into the expo")
    @GetMapping("/expoServices/{expoId}")
    public ResponseEntity<ApiResponse> getAllExpoServices(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(expoDaoIml.getAllEnableServices(expoId)).build());
    }

    @Operation(summary = "Get all Expo")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExpoByIds() {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(expoDaoIml.all()).build());
    }

    @Operation(summary = "Update Expo")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateExpo(@RequestBody ExpoDto expo, @PathVariable Long id) throws CustomException {
        expoDaoIml.saveAndFlush(expo, id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED).build());
    }

    @Operation(summary = "Delete Expo by id. All data will be deleted as well")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExpoById(@PathVariable Long id) throws CustomException {
        expoDaoIml.removeById(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED).build());
    }

    @Operation(summary = "Add service into a Expo by expo id and service name")
    @PutMapping("/addExpoService/{expoId}/{serviceId}")
    public ResponseEntity<ApiResponse> addExpoService(@PathVariable Long serviceId, @PathVariable Long expoId) throws CustomException {
        expoDaoIml.addExpoService(serviceId, expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Delete service from Expo")
    @PutMapping("/deleteService/{expoId}/{serviceId}")
    public ResponseEntity<ApiResponse> deleteServiceByName(@PathVariable Long expoId, @PathVariable Long serviceId) throws CustomException {
        expoDaoIml.deleteServiceById(expoId, serviceId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Check a service has taken or not")
    @GetMapping("/isExpoServiceTaken/{expoId}/{serviceId}")
    public ResponseEntity<ApiResponse> isExpoServiceTaken(@PathVariable Long expoId, @PathVariable Long serviceId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(expoDaoIml.isExpoServiceTaken(expoId, serviceId))
                .build());

    }
}
