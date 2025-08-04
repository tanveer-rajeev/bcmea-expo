package com.betafore.evoting.SmsService;

import com.betafore.evoting.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMS_SendServiceImpl implements SmsSendService {

    private final SmsSettingsRepository smsSettingsRepository;

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
