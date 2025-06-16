package com.betafore.evoting.SmsConfig;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomCipherService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMS_SendServiceImpl implements SmsSendService {

    private final SmsSettingsRepository smsSettingsRepository;
    private final CustomCipherService customCipherService;

    @Override
    @Transactional
    public void sendSms(Long expoId, String phoneNumber, String message) throws CustomException {
        try {
            SmsSettings smsSettings = smsSettingsRepository.findByExpoId(expoId).orElseThrow(() -> new CustomException("Sms setting not found by given id"));
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                .add("token", smsSettings.getSmsToken())
                .add("to", phoneNumber)
                .add("message", message)
                .build();
            log.info("SMS requestBody is ready [{}] ", requestBody);

            Request postRequest = new Request.Builder()
                .url(smsSettings.getSmsRequestUrl())
                .post(requestBody)
                .build();

            Response response = client.newCall(postRequest).execute();
            String responseBody = response.body().string();
            log.info("SMS response body [{}] ", responseBody);

            if (!response.isSuccessful()) {
                log.error("SMS goes wrong [{}]", false);
                throw new CustomException("Sms sending failed. Response: " + responseBody);
            }
        } catch (IOException e) {

            throw new CustomException("Sms sending operation goes wrong: " + e.getMessage());
        }

    }

}
