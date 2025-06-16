package com.betafore.evoting.SmsConfig;
import com.betafore.evoting.Exception.CustomException;

import java.io.IOException;
import java.util.List;

public interface  SmsSettingsService {

    SmsSettings createSmsSettings(Long expoId,SmsSettingsDto smsSettingsDto) throws CustomException;
    void updateSmsSettings(Integer id,SmsSettingsDto smsSettingsDto) throws CustomException, CustomException;
    void deleteSmsSettings(Integer id);
    SmsSettings getByIdSmsSettings(Integer id) throws CustomException;
    SmsSettings getByExpoIdSmsSettings(Long id) throws CustomException;

}
