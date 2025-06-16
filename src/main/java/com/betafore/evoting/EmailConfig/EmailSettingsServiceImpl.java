package com.betafore.evoting.EmailConfig;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomCipherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailSettingsServiceImpl implements EmailSettingsService {

    private final EmailSettingsRepository emailSettingsRepository;

    @Override
    public EmailSettings create(Long expoId, EmailDto emailDto) throws CustomException {
        Optional<EmailSettings> bySmtpUsername = emailSettingsRepository.findByExpoIdAndActive(expoId);
        if (bySmtpUsername.isPresent()) {
            throw new CustomException("Email configuration/setting is already present: " + emailDto.getSmtpUsername());
        }

        EmailSettings emailSettings = EmailSettings.builder()
            .expoId(expoId)
            .smtpMailServer(emailDto.getSmtpMailServer())
            .emailProfileName(emailDto.getEmailProfileName())
            .smtpPort(emailDto.getSmtpPort())
            .smtpUsername(emailDto.getSmtpUsername())
            .smtpUserPassword(emailDto.getSmtpUserPassword())
            .sslOrTls(emailDto.getSslOrTls())
            .active(Boolean.parseBoolean(emailDto.getActive()))
            .build();

        emailSettingsRepository.save(emailSettings);
        return emailSettings;
    }

    @Transactional
    @Override
    public void update(EmailDto emailSettingsDto, Long id) throws CustomException {
        EmailSettings settings = emailSettingsRepository.findById(id).orElseThrow(() -> new CustomException("Email settings not found by given id"));

        if (emailSettingsDto.getEmailProfileName() != null && !emailSettingsDto.getEmailProfileName().isEmpty()
            && !emailSettingsDto.getEmailProfileName().equals(settings.getEmailProfileName())) {
            settings.setEmailProfileName(emailSettingsDto.getEmailProfileName());
        }
        if (emailSettingsDto.getSmtpUserPassword() != null && !emailSettingsDto.getSmtpUserPassword().isEmpty()) {
            settings.setSmtpUserPassword(emailSettingsDto.getSmtpUserPassword());
        }
        if (emailSettingsDto.getSmtpMailServer() != null && !emailSettingsDto.getSmtpMailServer().isEmpty()
            && !emailSettingsDto.getSmtpMailServer().equals(settings.getSmtpMailServer())) {
            settings.setSmtpMailServer(emailSettingsDto.getSmtpMailServer());
        }
        if (emailSettingsDto.getSmtpPort() != null && !emailSettingsDto.getSmtpPort().isEmpty() &&
            !emailSettingsDto.getSmtpPort().equals(settings.getSmtpPort())) {
            settings.setSmtpPort(emailSettingsDto.getSmtpPort());
        }
        if (emailSettingsDto.getSmtpUsername() != null && !emailSettingsDto.getSmtpUsername().isEmpty()
            && !emailSettingsDto.getSmtpUsername().equals(settings.getSmtpUsername())) {
            settings.setSmtpUsername(emailSettingsDto.getSmtpUsername());
        }
        if (emailSettingsDto.getSslOrTls() != null && !emailSettingsDto.getSslOrTls().isEmpty() &&
            !emailSettingsDto.getSslOrTls().equals(settings.getSslOrTls())) {
            settings.setSslOrTls(emailSettingsDto.getSslOrTls());
        }
        if (emailSettingsDto.getActive() != null && !emailSettingsDto.getActive().isEmpty()){
            settings.setActive(Boolean.parseBoolean(emailSettingsDto.getActive()));
        }

    }

    @Override
    public void delete(Long id) {
        emailSettingsRepository.deleteById(id);
    }

    @Override
    public EmailSettings getById(Long id) throws CustomException {
        return emailSettingsRepository.findById(id).orElseThrow(() -> new CustomException("Email setting not found"));
    }

    @Override
    public EmailSettings getByExpoId(Long id) throws CustomException {
        return emailSettingsRepository.findByExpoIdAndActive(id).orElseThrow(() ->
            new CustomException("email settings not found in the expo")
        );
    }
}
