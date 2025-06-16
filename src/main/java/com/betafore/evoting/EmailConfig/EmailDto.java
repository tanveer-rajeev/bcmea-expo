package com.betafore.evoting.EmailConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String emailProfileName;
    private String smtpMailServer;
    private String smtpUserPassword;
    private String smtpUsername;
    private String sslOrTls;
    private String smtpPort;
    private String active;
}
