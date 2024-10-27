package org.example.footballanalyzer;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.Service.Auth.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class FootballAnalyzerApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String apiKeysString = dotenv.get("API_KEYS");
        assert apiKeysString != null;
        String[] apiKeys = apiKeysString.split(",");
        for (int i = 0; i < apiKeys.length; i++) {
            System.setProperty("api.key" + (i + 1), apiKeys[i]);
        }
        String jwtSecret = dotenv.get("JWT_SECRET");
        assert jwtSecret != null;
        JwtService.setSECRET(jwtSecret);

        SpringApplication.run(FootballAnalyzerApplication.class, args);
    }

}
