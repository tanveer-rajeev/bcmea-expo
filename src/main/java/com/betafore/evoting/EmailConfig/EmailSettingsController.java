package com.betafore.evoting.EmailConfig;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.Common.ResponseMessageConstants;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/email/settings")
public class EmailSettingsController {

    private final EmailSettingsService emailSettingsService;
    private final EmailSenderService emailSenderService;

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_CREATE)
    @PostMapping("/sendEmail/{expoId}")
    public ResponseEntity<ApiResponse> sendMail(@RequestBody SendEmailDto sendEmailDto,
                                                @PathVariable Long expoId) throws CustomException {
        emailSenderService.sendEmail(sendEmailDto, expoId);
        return ResponseEntity.ok().body(ApiResponse.builder()
            .success(true)
            .message(ResponseMessageConstants.EMAIL_SEND_SUCCESS)
            .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> createEmailSettings(@PathVariable Long expoId, @RequestBody EmailDto emailDto) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.CREATED)
                .data(emailSettingsService.create(expoId, emailDto))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEmailSettings(@PathVariable Long id, @RequestBody EmailDto emailDto) throws CustomException {
        emailSettingsService.update(emailDto, id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.UPDATED)
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmailSettings(@PathVariable Long id) {
        emailSettingsService.delete(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.DELETED)
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_READ)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmailSettings(@PathVariable Long id) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.DELETED)
                .data(emailSettingsService.getById(id))
                .build());
    }

    @CustomHasAuthority(authorities = PermissionEnum.SETTINGS_READ)
    @GetMapping("/expoId/{expoId}")
    public ResponseEntity<ApiResponse> getByExpoIdEmailSettings(@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.FETCHED)
                .data(emailSettingsService.getByExpoId(expoId))
                .build());
    }
}
