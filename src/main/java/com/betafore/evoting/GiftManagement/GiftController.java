package com.betafore.evoting.GiftManagement;

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
@RequestMapping("/api/v1/gifts")
public class GiftController {

    private final GiftService giftService;
    private final FileStorageServiceImp fileStorageServiceImp;

    @Operation(summary = "Create Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_CREATE)
    @PostMapping(path = "/{expoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> save(@Valid @ModelAttribute GiftDto giftDto,
                                            @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Gift created")
                .data(giftService.save(giftDto, expoId)).build());
    }

    @Operation(summary = "Set gift eligibility for all")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_CREATE)
    @PostMapping("/setEligibility/{giftId}/{eligibleFlag}")
    public ResponseEntity<ApiResponse> setEligibility(@PathVariable Long giftId, @PathVariable boolean eligibleFlag) throws CustomException {
        giftService.setEligibility(giftId, eligibleFlag);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Eligible set successfully")
                .build());
    }

    @Operation(summary = "Update Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_UPDATE)
    @PutMapping(path = "/{giftId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable Long giftId,
                                              @ModelAttribute GiftDto giftDto) throws CustomException {
        giftService.updateGift(giftId, giftDto);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Gift updated").build());
    }

    @Operation(summary = "Get Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_READ)
    @GetMapping("/{giftId}")
    public ResponseEntity<ApiResponse> getGiftById(@PathVariable Long giftId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Gift fetched")
                .data(giftService.findById(giftId)).build());
    }

    @Operation(summary = "Get all Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllGift(@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("All gift fetched")
                .data(giftService.all(expoId)).build());
    }

    @Operation(summary = "Delete Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_DELETE)
    @DeleteMapping("/{giftId}")
    public ResponseEntity<ApiResponse> deleteGiftById(@PathVariable Long giftId) throws CustomException {
        giftService.removeById(giftId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Gift deleted")
                .build());
    }

    @Operation(summary = "Delete all Gift by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllGift(@PathVariable Long expoId) throws CustomException {
        giftService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("All gift deleted")
                .build());
    }

    @Operation(summary = "User scan for Gift")
    @CustomHasAuthority(authorities = PermissionEnum.GIFT_READ)
    @GetMapping("/userScan/{giftId}/{userId}")
    public ResponseEntity<ApiResponse> giftScanner(@PathVariable Long giftId,
                                                   @PathVariable Long userId) throws CustomException {
        boolean result = giftService.giftScan(giftId, userId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(result)
                .message(result ? SCAN : SCAN_FAILED)
                .data(result).build());
    }

    @Operation(summary = "Get gift image")
    @GetMapping("/img/{giftId}")
    public ResponseEntity<?> getSeminarImage(@PathVariable Long giftId) throws CustomException {
        Gift gift = giftService.findById(giftId);
        if (gift.getImg() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
            fileStorageServiceImp.loadFile(gift.getImg())
        );
    }
}
