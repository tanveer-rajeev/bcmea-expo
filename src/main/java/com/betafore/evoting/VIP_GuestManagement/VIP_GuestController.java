package com.betafore.evoting.VIP_GuestManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vip")
public class VIP_GuestController {

    private final VIP_GuestService vip_guestService;
    private final FileStorageServiceImp fileStorageServiceImp;

    @Operation(summary = "Create vip by participant id and expo id")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_CREATE)
    @PostMapping("/{participantId}/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @ModelAttribute VIP_GuestDto vip_guest,
                                            @PathVariable Long participantId,
                                            @PathVariable Long expoId,
                                            @RequestParam(required = false) String token) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(vip_guestService.save(vip_guest, participantId, expoId))
                .build());
    }

    @Operation(summary = "Create Uri by participant id and expo id for vip guest as if vip guest fill up the form without authentication")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_READ)
    @GetMapping("/getRegisterUri/{participantId}/{expoId}")
    public ResponseEntity<ApiResponse> createUriForVIP_Guest(@PathVariable Long participantId,
                                                             @PathVariable Long expoId, @NotNull HttpServletRequest request) {
        return ResponseEntity.status(201)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(vip_guestService.createUriForVip(participantId,expoId,request))
                .build());
    }

    @Operation(summary = "Get all employee by participant id and expo id")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_READ)
    @GetMapping("/all/byParticipant/{participantId}")
    public ResponseEntity<ApiResponse> getAllEmployeeByParticipant(@PathVariable Long participantId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(vip_guestService.findByParticipantIdAndExpoId(participantId))
                .build());
    }

    @Operation(summary = "Update vip")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_UPDATE)
    @PutMapping("/{vip_guestId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long vip_guestId, @ModelAttribute VIP_GuestDto vipGuestDto) throws CustomException {
        vip_guestService.updateVIP_Guest(vip_guestId, vipGuestDto);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get vip by id")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_READ)
    @GetMapping("/{vip_guestId}")
    public ResponseEntity<ApiResponse> getVIP_GuestById(@PathVariable Long vip_guestId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(vip_guestService.findById(vip_guestId))
                .build());
    }

    @Operation(summary = "Get all vip by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllVIP_Guest(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(vip_guestService.all(expoId))
                .build());
    }

    @Operation(summary = "Delete vip")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_DELETE)
    @DeleteMapping("/{vip_guestId}")
    public ResponseEntity<ApiResponse> deleteVIP_GuestById(@PathVariable Long vip_guestId) throws CustomException {
        vip_guestService.removeById(vip_guestId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all vip by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.VIP_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllVIP_Guest(@PathVariable Long expoId) throws CustomException {
        vip_guestService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Get vip guest image")
    @GetMapping("/img/{vipId}")
    public ResponseEntity<?> getSeminarImage(@PathVariable Long vipId) throws CustomException {
        VIP_Guest vipGuest = vip_guestService.findById(vipId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
            fileStorageServiceImp.loadFile(vipGuest.getImg())
        );
    }
}
