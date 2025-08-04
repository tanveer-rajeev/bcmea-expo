package com.betafore.evoting.EmailService;

import com.betafore.evoting.Exception.CustomException;
import jakarta.mail.Session;

import java.util.Properties;

public interface EmailSenderService {
    void sendEmail(SendEmailDto sendEmailDto, Long expoId) throws CustomException;
    void sendByTSL(EmailSettings emailSettings,String smtpUserPassword, SendEmailDto sendEmailDto)throws CustomException;
    void sendBySSL(EmailSettings emailSettings,String smtpUserPassword, SendEmailDto sendEmailDto)throws CustomException;
    Properties getSSLProperties(EmailSettings emailSettings);
    void sendAttachmentEmail(Session session, String toEmail, String subject, String body);
    void sendImageEmail(Session session, String toEmail, String subject, String body);
}
