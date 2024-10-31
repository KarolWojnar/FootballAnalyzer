package org.example.footballanalyzer.Config;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    private final String email;
    private final String password;
    private Authenticator authenticator;
    private Session session;
    private Properties properties;

    public EmailConfiguration(@Value("${notify.mail}") String email, @Value("${notify.mail.password}") String password) {
        this.email = email;
        this.password = password;
        properties = new Properties();
        config();
    }

    private void config() {
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        this.authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };
    }

    private void refreshSession() {
        session = Session.getInstance(properties, authenticator);
    }

    public void sendMail(String recipientEmail, String content, String subject, boolean onCreate) {
        if (session == null) {
            refreshSession();
        }
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            if (onCreate) {
                config();
                sendMail(recipientEmail, content, subject, false);
            }
        }
    }
}
