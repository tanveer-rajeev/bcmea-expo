package com.betafore.evoting.EmailService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendEmailDto {

    private String to;
    private String subject;
    private String body;
}
