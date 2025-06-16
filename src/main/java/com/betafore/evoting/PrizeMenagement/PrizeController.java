package com.betafore.evoting.PrizeMenagement;

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
@RequestMapping("/api/v1/prizes")
public class PrizeController {

    private final PrizeService prizeService;
    private final FileStorageServiceImp fileStorageServiceImp;

    @Operation(summary = "Create prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @ModelAttribute PrizeDto prizeDto,
                                            @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(prizeService.save(prizeDto,expoId))
                .build());
    }

    @Operation(summary = "Update prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_UPDATE)
    @PutMapping("/{prizeId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long prizeId, @ModelAttribute PrizeDto prizeDto) throws CustomException {
        prizeService.update(prizeDto, prizeId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_READ)
    @GetMapping("/{prizeId}")
    public ResponseEntity<ApiResponse> getPrizeById(@PathVariable Long prizeId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(prizeService.getPrizeById(prizeId))
                .build());
    }

    @Operation(summary = "Get all prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllPrize(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(prizeService.all(expoId))
                .build());
    }

    @Operation(summary = "Delete prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_DELETE)
    @DeleteMapping("/{prizeId}")
    public ResponseEntity<ApiResponse> deletePrizeById(@PathVariable Long prizeId) throws CustomException {
        prizeService.removeById(prizeId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all prize")
    @CustomHasAuthority(authorities = PermissionEnum.PRIZE_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllPrize(@PathVariable Long expoId) throws CustomException {
        prizeService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }


    @Operation(summary = "Get prize image")
    @GetMapping("/img/{prizeId}")
    public ResponseEntity<?> getPrizeImage(@PathVariable Long prizeId) throws CustomException {
        Prize prize = prizeService.findById(prizeId);
        if (prize.getImg() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(fileStorageServiceImp.loadFile(prize.getImg()));
    }

}
