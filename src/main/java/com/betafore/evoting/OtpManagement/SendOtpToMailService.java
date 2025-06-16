package com.betafore.evoting.OtpManagement;

import com.betafore.evoting.EmailConfig.EmailSenderService;
import com.betafore.evoting.EmailConfig.SendEmailDto;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.OrganizerManagement.Organizer;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import com.betafore.evoting.Util.OtpGenerateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("sendOtpToMailService")
@RequiredArgsConstructor
public class SendOtpToMailService implements OtpService{

    private final OtpRepository otpRepository;
    private final OrganizerRepository organizerRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public String sendOtp(Long expoId, String email) throws CustomException {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new CustomException("Email not valid..");
        }

        Organizer organizer = organizerRepository.findOrganizerByEmail(email).orElseThrow(() -> new CustomException("Email not found in the system"));
        String otp = OtpGenerateUtil.generateOTP();
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);

        if (optionalOtpEntity.isPresent()) {
            OtpEntity otpEntity = optionalOtpEntity.get();
            otpEntity.setOtp(otp);
        } else {
            OtpEntity otpEntity = OtpEntity.builder().email(email).phoneNumber(organizer.getPhoneNumber()).otp(otp).build();
            otpRepository.save(otpEntity);
        }

        SendEmailDto sendEmailDto = SendEmailDto.builder().to(email).subject("User verification")
            .body(otp).build();

        emailSenderService.sendEmail(sendEmailDto, organizer.getExpoId());
        return otp;
    }

    @Override
    public boolean otpValidation(OtpRequestDto otpRequestDto) throws CustomException {
        OtpEntity otpEntity  = otpRepository.findByEmail(otpRequestDto.getEmail())
                .orElseThrow(() -> new CustomException("Email not found"));

        if (otpEntity.getOtp().equals(otpRequestDto.getOtp())) {
            otpRepository.delete(otpEntity);
            return true;
        }else {
            throw new CustomException("Otp not validated");
        }
    }

    @Override
    public void saveOtp(String otp, String email){
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        if (optionalOtpEntity.isPresent()) {
            OtpEntity otpEntity = optionalOtpEntity.get();
            otpEntity.setOtp(otp);
        } else {
            OtpEntity otpEntity = OtpEntity.builder()
                .phoneNumber(email).otp(otp).build();
            otpRepository.save(otpEntity);
        }
    }
}
