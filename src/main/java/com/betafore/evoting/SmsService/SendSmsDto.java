package com.betafore.evoting.SmsService;

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
public class SendSmsDto {
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^([01]|\\+88)?\\d{11}",message = "phone number not valid")
    private String phoneNumber;
    private String message;
}
