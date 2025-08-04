package com.betafore.evoting.SmsService;

import com.betafore.evoting.Exception.CustomException;

public interface SmsSendService {
    void sendSms(Long expoId,String phoneNumber,String message) throws CustomException;
}
