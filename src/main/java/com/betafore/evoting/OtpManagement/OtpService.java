package com.betafore.evoting.OtpManagement;

import com.betafore.evoting.Exception.CustomException;

public interface OtpService {
    public String sendOtp(Long expoId,String medium) throws CustomException;
    public boolean otpValidation(OtpRequestDto otpRequestDto) throws CustomException;
    public void saveOtp(String otp,String medium) throws CustomException;
}
