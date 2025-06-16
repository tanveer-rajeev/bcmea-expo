package com.betafore.evoting.OrganizerManagement;

import com.betafore.evoting.security_config.OtpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponseDto {

    private OtpStatus otpStatus;
    private String message;
}
