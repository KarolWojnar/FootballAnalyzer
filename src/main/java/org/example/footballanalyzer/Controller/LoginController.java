package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.LoginApi;
import org.example.footballanalyzer.Service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {
    private final LoginService loginService;

    @Override
    public ResponseEntity<String> hello(String name, String password) {
        return ResponseEntity.ok(loginService.login(name, password));
    }
}
