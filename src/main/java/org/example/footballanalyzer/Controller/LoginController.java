package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.LoginApi;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {
    private final LoginService loginService;

    @Override
    public ResponseEntity<?> login() {
        String login = "";
        String password = "";
        return loginService.generateToken(login, password);
    }

    @Override
    public ResponseEntity<String> register(UserDTO user) {
        return loginService.register(user);
    }
}
