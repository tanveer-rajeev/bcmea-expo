package com.betafore.evoting.SeminarManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seminars")
public class SeminarController {

    private final SeminarService seminarService;
    private final FileStorageServiceImp fileStorageServiceImp;

    @Operation(summary = "Save Seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @ModelAttribute SeminarDto seminarDto,
                                            @PathVariable Long expoId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(seminarService.save(seminarDto,expoId)).build());
    }

    @Operation(summary = "Update Seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_UPDATE)
    @PutMapping("/{seminarId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long seminarId, @ModelAttribute SeminarDto seminarDto) throws CustomException {
        seminarService.update(seminarId, seminarDto);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED).build());
    }

    @Operation(summary = "Get Seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_READ)
    @GetMapping("/{seminarId}")
    public ResponseEntity<ApiResponse> getSeminarById(@PathVariable Long seminarId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(seminarService.findById(seminarId)).build());
    }

    @Operation(summary = "Get all Seminar by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllSeminar(@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(seminarService.all(expoId)).build());
    }

    @Operation(summary = "Delete Seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_DELETE)
    @DeleteMapping("/{seminarId}")
    public ResponseEntity<ApiResponse> deleteSeminarById(@PathVariable Long seminarId) throws CustomException {
        seminarService.removeById(seminarId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED).build());
    }

    @Operation(summary = "Delete all question")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllSeminar(@PathVariable Long expoId) throws CustomException {
        seminarService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED).build());
    }

    @Operation(summary = "Check availability for a seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_READ)
    @GetMapping("/available/{expoId}")
    public ResponseEntity<ApiResponse> getAvailableSeminar(@PathVariable Long expoId) {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(seminarService.getAvailableSeminars(expoId)).build());
    }

    @Operation(summary = "User scan for seminar")
    @CustomHasAuthority(authorities = PermissionEnum.SEMINAR_READ)
    @GetMapping("/userScan/{seminarId}/{userId}")
    public ResponseEntity<ApiResponse> seminarAccessVerification(@PathVariable Long seminarId, @PathVariable Long userId) throws CustomException {
        boolean result = seminarService.seminarAccessVerification(seminarId, userId);

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(result)
                .message(result ? SCAN : SCAN_FAILED)
                .data(result).build());
    }

    @Operation(summary = "Get seminar image")
    @GetMapping("/img/{seminarId}")
    public ResponseEntity<?> getSeminarImage(@PathVariable Long seminarId) throws CustomException {
        Seminar seminar = seminarService.findById(seminarId);
        if (seminar.getImg() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(fileStorageServiceImp.loadFile(seminar.getImg()));
    }
}
