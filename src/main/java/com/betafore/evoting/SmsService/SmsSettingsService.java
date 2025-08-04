package com.betafore.evoting.SmsService;
import com.betafore.evoting.Exception.CustomException;

public interface  SmsSettingsService {

    SmsSettings createSmsSettings(Long expoId,SmsSettingsDto smsSettingsDto) throws CustomException;
    void updateSmsSettings(Integer id,SmsSettingsDto smsSettingsDto) throws CustomException, CustomException;
    void deleteSmsSettings(Integer id);
    SmsSettings getByIdSmsSettings(Integer id) throws CustomException;
    SmsSettings getByExpoIdSmsSettings(Long id) throws CustomException;

}
