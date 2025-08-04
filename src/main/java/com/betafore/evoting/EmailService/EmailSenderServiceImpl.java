package com.betafore.evoting.EmailService;

import com.betafore.evoting.Exception.CustomException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {
    private final EmailSettingsRepository emailSettingsRepository;

    @Transactional
    @Override
    public void sendEmail(SendEmailDto sendEmailDto, Long expoId) throws CustomException {
        EmailSettings emailSettings = emailSettingsRepository.findByExpoIdAndActive(expoId).orElseThrow(() ->
            new CustomException("Email settings not found by given id or is not active")
        );
        if (!emailSettings.isActive()) {
            throw new CustomException("Email setting is inactive");
        }
        if (emailSettings.getSslOrTls().equalsIgnoreCase("ssl"))
            sendBySSL(emailSettings, emailSettings.getSmtpUserPassword(), sendEmailDto);
        else
            sendByTSL(emailSettings, emailSettings.getSmtpUserPassword(), sendEmailDto);
    }

    private void sendEmail(Session session, String toEmail, String subject, String body, String profileName, String from) throws CustomException {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(from, profileName));

            msg.setReplyTo(InternetAddress.parse(from, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            log.info("Message is ready to send");
            Transport.send(msg);
            log.info("Email Sent Successfully!!");
        } catch (Exception e) {
            log.error("Problem occurred while sending email");
            throw new CustomException("Sending email failed");
        }
    }

    public void sendByTSL(EmailSettings emailSettings, String smtpUserPassword, SendEmailDto sendEmailDto) throws CustomException {

        log.info("Email process started..");

        Properties props = new Properties();
        props.put("mail.smtp.host", emailSettings.getSmtpMailServer());
        props.put("mail.smtp.port", emailSettings.getSmtpPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSettings.getSmtpUsername(),
                    smtpUserPassword);
            }
        };
        Session session = Session.getInstance(props, auth);

        sendEmail(session, sendEmailDto.getTo(), sendEmailDto.getSubject(), sendEmailDto.getBody(), emailSettings.getEmailProfileName(), emailSettings.getSmtpUsername());

    }

    public void sendBySSL(EmailSettings emailSettings, String smtpUserPassword, SendEmailDto sendEmailDto) throws CustomException {
        log.info("Email process started..");

        Properties props = getSSLProperties(emailSettings);

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSettings.getSmtpUsername(),
                    smtpUserPassword);
            }
        };
        Session session = Session.getInstance(props, auth);

        sendEmail(session, sendEmailDto.getTo(), sendEmailDto.getSubject(), sendEmailDto.getBody(),
            emailSettings.getEmailProfileName(), emailSettings.getSmtpUsername());

    }

    public Properties getSSLProperties(EmailSettings emailSettings) {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailSettings.getSmtpMailServer());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", emailSettings.getSmtpPort()); //SSL Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port
        return props;
    }

    public void sendAttachmentEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "abc.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart);

            // Send message
            Transport.send(msg);
            System.out.println("EMail Sent Successfully with attachment!!");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendImageEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is image attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "image.png";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            //Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);

            //third part for displaying image in the email body
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h1>Attached Image</h1>" +
                "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);

            //Set the multipart message to the email message
            msg.setContent(multipart);

            // Send message
            Transport.send(msg);
            System.out.println("EMail Sent Successfully with image!!");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}



