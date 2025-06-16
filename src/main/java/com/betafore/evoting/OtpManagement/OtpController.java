package com.betafore.evoting.OtpManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.Common.ResponseMessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    private final SendOtpToPhoneService otpServiceForPhone;

    private final SendOtpToMailService otpServiceForMail;

    @Operation(summary = "Send otp to phone number")
    @PostMapping("/sendToPhone/{phoneNumber}/{expoId}")
    public ResponseEntity<ApiResponse> sendOtpToPhone(@Valid @PathVariable String phoneNumber, @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.SEND_OTP)
                .data(otpServiceForPhone.sendOtp(expoId, phoneNumber)).build());
    }

    @Operation(summary = "Send otp to email")
    @PostMapping("/sendToMail/{email}/{expoId}")
    public ResponseEntity<ApiResponse> sendOtpToEmail(@PathVariable String email, @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.SEND_OTP)
                .data(otpServiceForMail.sendOtp(expoId, email))
                .build());
    }

    @Operation(summary = "Otp Validation by phone number ")
    @PostMapping("/validationByPhone")
    public ResponseEntity<ApiResponse> otpValidationByPhoneNumber(@RequestBody OtpRequestDto otpRequestDto) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.OTP_VALIDATION)
                .data(otpServiceForPhone.otpValidation(otpRequestDto)).build());
    }

    @Operation(summary = "Otp Validation by email")
    @PostMapping("/validationByEmail")
    public ResponseEntity<ApiResponse> otpValidationByMail(@RequestBody OtpRequestDto otpRequestDto) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(ResponseMessageConstants.OTP_VALIDATION)
                .data(otpServiceForMail.otpValidation(otpRequestDto)).build());
    }
}
