package com.betafore.evoting.SmsConfig;

import com.betafore.evoting.Exception.CustomException;

public interface SmsSendService {
    void sendSms(Long expoId,String phoneNumber,String message) throws CustomException;
}
