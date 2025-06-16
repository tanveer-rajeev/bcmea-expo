package com.betafore.evoting.OrganizerManagement;

import lombok.Data;

@Data
public class PasswordResetDto {

    private String phoneNumber;
    private String email;
    private String otp;
}
