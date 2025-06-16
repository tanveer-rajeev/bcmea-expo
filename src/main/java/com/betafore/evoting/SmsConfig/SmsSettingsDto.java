package com.betafore.evoting.SmsConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsSettingsDto {

    private String smsRequestUrl;

    private String smsToken;

}
