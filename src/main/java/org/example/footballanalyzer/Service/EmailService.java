package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.Config.EmailConfiguration;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailConfiguration emailConfiguration;
    @Value("classpath:static/mail-active.html")
    Resource activeTemplate;
    @Value("classpath:static/reset-password.html")
    Resource resetPasswordTemplate;
    @Value("${front.url}")
    private String frontUrl;

    public void sedActivation(UserEntity user) {
        try {
            String html = Files.readString(activeTemplate.getFile().toPath(), StandardCharsets.UTF_8);
            html = html.replace("https://google.com", frontUrl + "/active/" + user.getUuid());
            emailConfiguration.sendMail(user.getEmail(), html, "Aktywacja konta", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sedPasswordRecovery(UserEntity user, String uuid) throws IOException {
        String html = Files.readString(resetPasswordTemplate.getFile().toPath(), StandardCharsets.UTF_8);
        html = html.replace("https://google.com", frontUrl + "/forgot-password/" + uuid);
        emailConfiguration.sendMail(user.getEmail(), html, "Odzyskaj hasło", true);
    }
}
