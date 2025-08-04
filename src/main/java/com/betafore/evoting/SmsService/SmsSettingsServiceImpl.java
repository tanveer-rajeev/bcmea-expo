package com.betafore.evoting.SmsService;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomCipherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsSettingsServiceImpl implements SmsSettingsService {

    private final SmsSettingsRepository smsSettingsRepository;
    private final CustomCipherService customCipherService;

    @Override
    public SmsSettings createSmsSettings(Long expoId, SmsSettingsDto smsSettingsDto) throws CustomException {

        Optional<SmsSettings> byExpoId = smsSettingsRepository.findByExpoId(expoId);
        if (byExpoId.isPresent()) {
            throw new CustomException("Sms configuration/setting is already present");
        }
        log.info("Sms settings saved");
        SmsSettings smsSettings = SmsSettings.builder()
            .expoId(expoId)
            .smsRequestUrl(smsSettingsDto.getSmsRequestUrl())
            .smsToken(smsSettingsDto.getSmsToken())
            .build();
        return smsSettingsRepository.save(smsSettings);
    }

    @Transactional
    @Override
    public void updateSmsSettings(Integer id, SmsSettingsDto smsSettingsDto) throws CustomException {
        SmsSettings settings = smsSettingsRepository.findById(id).orElseThrow(() -> new CustomException("Sms settings not found by given id: " + id));
        if (smsSettingsDto.getSmsRequestUrl() != null && !smsSettingsDto.getSmsRequestUrl().isEmpty() &&
            !smsSettingsDto.getSmsRequestUrl().equals(settings.getSmsRequestUrl())) {
            settings.setSmsRequestUrl(smsSettingsDto.getSmsRequestUrl());
        }
        if (smsSettingsDto.getSmsToken() != null && !smsSettingsDto.getSmsToken().isEmpty()) {
            settings.setSmsToken(smsSettingsDto.getSmsToken());
        }

    }

    @Override
    public void deleteSmsSettings(Integer id) {
        smsSettingsRepository.deleteById(id);
    }

    @Override
    public SmsSettings getByIdSmsSettings(Integer id) throws CustomException {
        return smsSettingsRepository.findById(id).orElseThrow(() -> new CustomException("Sms setting not found by given id: " + id));
    }

    @Override
    public SmsSettings getByExpoIdSmsSettings(Long id) throws CustomException {
        return smsSettingsRepository.findByExpoId(id).orElseThrow(() -> new CustomException("Sms setting not found by given id: " + id));
    }

}
