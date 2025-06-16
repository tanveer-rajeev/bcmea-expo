package com.betafore.evoting.OtpManagement;

import com.betafore.evoting.EmailConfig.EmailSenderService;
import com.betafore.evoting.EmailConfig.SendEmailDto;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.SmsConfig.SMS_SendServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.betafore.evoting.Util.OtpGenerateUtil.generateOTP;

@Service
@RequiredArgsConstructor
public class SendOtpToPhoneService implements OtpService {

    private final SMS_SendServiceImpl smsSendService;
    private final OtpRepository otpRepository;

    @Transactional
    public void saveOtp(String otp, String phoneNumber) {

        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByPhoneNumber(phoneNumber);
        if (optionalOtpEntity.isPresent()) {
            OtpEntity otpEntity = optionalOtpEntity.get();
            otpEntity.setOtp(otp);
        } else {
            OtpEntity otpEntity = OtpEntity.builder()
                .phoneNumber(phoneNumber).otp(otp).build();
            otpRepository.save(otpEntity);
        }

    }

    @Override
    @Transactional
    public String sendOtp(Long expoId, String medium) throws CustomException {
        Pattern pattern = Pattern.compile("^([01]|\\+88)?\\d{11}");
        Matcher matcher = pattern.matcher(medium);

        if (!matcher.matches()) {
            throw new CustomException("Phone number not valid..");
        }
        String otp = generateOTP();
        String otpMessage = "(Betafore) Your OTP is " + otp + ".Thank you";

        saveOtp(otp, medium);
        smsSendService.sendSms(expoId, medium, otpMessage);
        return otp;
    }

    @Override
    public boolean otpValidation(OtpRequestDto otpRequestDto) throws CustomException {

        OtpEntity otpEntity = otpRepository.findByPhoneNumber(otpRequestDto.getPhoneNumber())
            .orElseThrow(() -> new CustomException("Phone number not present"));

        if (otpEntity.getOtp().equals(otpRequestDto.getOtp())) {
            otpRepository.delete(otpEntity);
            return true;
        } else {
            throw new CustomException("Otp not validated");
        }

    }

}
