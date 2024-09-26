package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.API.AuthApi;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.LoginRequest;
import org.example.footballanalyzer.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest);
        return authService.generateToken(loginRequest);
    }

    @Override
    public ResponseEntity<String> register(UserDTO user) {
        return authService.register(user);
    }
}
