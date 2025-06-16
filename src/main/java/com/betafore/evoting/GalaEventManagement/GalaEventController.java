package com.betafore.evoting.GalaEventManagement;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gala")
public class GalaEventController {

    private final GalaEventService galaEventService;
    private final FileStorageServiceImp fileStorageServiceImp;

    @Operation(summary = "Create Gala-Event by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @ModelAttribute GalaEventDto galaEventDto,
                                            @PathVariable Long expoId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(galaEventService.save(galaEventDto,expoId))
                .build());
    }

    @Operation(summary = "Update Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_UPDATE)
    @PutMapping("/{galaEventId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long galaEventId, @ModelAttribute GalaEventDto galaEventDto) throws CustomException {
        galaEventService.update(galaEventId, galaEventDto);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_READ)
    @GetMapping("/{galaEventId}")
    public ResponseEntity<ApiResponse> getGalaEventById(@PathVariable Long galaEventId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(galaEventService.findById(galaEventId))
                .build());
    }

    @Operation(summary = "Get all Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllGalaEvent(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(galaEventService.all(expoId))
                .build());
    }

    @Operation(summary = "Delete Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_DELETE)
    @DeleteMapping("/{galaEventId}")
    public ResponseEntity<ApiResponse> deleteGalaEventById(@PathVariable Long galaEventId) throws CustomException {
        galaEventService.removeById(galaEventId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllGalaEvent(@PathVariable Long expoId) {
        galaEventService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "User scan for Gala-Event")
    @CustomHasAuthority(authorities = PermissionEnum.GALA_READ)
    @GetMapping("/userScan/{galaId}/{userId}")
    public ResponseEntity<ApiResponse> galaEventAccessVerification(@PathVariable Long galaId,@PathVariable Long userId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(SCAN)
                .data(galaEventService.galaEventAccessVerification(galaId,userId))
                .build());
    }

    @Operation(summary = "Get gala event image")
    @GetMapping("/img/{galaEventId}")
    public ResponseEntity<?> getSeminarImage(@PathVariable Long galaEventId) throws CustomException {
        GalaEvent galaEvent = galaEventService.findById(galaEventId);
        if (galaEvent.getImg() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG).body(
               fileStorageServiceImp.loadFile(galaEvent.getImg())
            );
    }
}
