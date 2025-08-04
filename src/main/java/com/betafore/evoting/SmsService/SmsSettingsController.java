package com.betafore.evoting.SmsService;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sms/settings")
public class SmsSettingsController {

    private final SmsSettingsService service;

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> createSmsSettings(@PathVariable Long expoId,@RequestBody SmsSettingsDto smsSettings) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .data(service.createSmsSettings(expoId,smsSettings))
                .message(CREATED)
                .build());
    }
    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSmsSettings(@PathVariable Integer id,
                                                         @RequestBody SmsSettingsDto smsSettings) throws CustomException {
        service.updateSmsSettings(id, smsSettings);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_READ)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSmsSettings(@PathVariable Integer id) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .data(service.getByIdSmsSettings(id))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_READ)
    @GetMapping("/expoId/{id}")
    public ResponseEntity<ApiResponse> getSmsSettingsByExpoId(@PathVariable Long id) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(service.getByExpoIdSmsSettings(id))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) throws CustomException {
        service.deleteSmsSettings(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }
}
