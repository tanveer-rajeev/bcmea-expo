package com.betafore.evoting.OtpManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequestDto {

    @Pattern(regexp = "^([01]|\\+88)?\\d{11}",message = "phone number not valid")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email should match the pattern a-z @ a-z .com")
    private String email;

    @NotBlank(message = "Otp is mandatory")
    private String otp;
}
