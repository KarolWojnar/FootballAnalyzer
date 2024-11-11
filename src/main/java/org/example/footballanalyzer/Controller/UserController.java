package org.example.footballanalyzer.Controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.UserApi;
import org.example.footballanalyzer.Data.ChangePasswordData;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserLoginData;
import org.example.footballanalyzer.Data.Dto.UserRequesetDto;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Data.ResetPasswordMail;
import org.example.footballanalyzer.Data.ValidationMessage;
import org.example.footballanalyzer.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<?> createUser(UserDTO user) {
        return userService.createUser(user);
    }

    @Override
    public ResponseEntity<?> login(UserLoginData user, HttpServletResponse response) {
        try {
            return userService.login(user, response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A1));
        }
    }

    @Override
    public ResponseEntity<?> getRole() {
        return userService.getRole();
    }

    @Override
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return userService.loginByToken(request, response);
    }

    @Override
    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        return userService.loggedIn(request, response);
    }

    @Override
    public ResponseEntity<AuthResponse> validate(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.validateToken(request, response);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A3));
        }
    }

    @Override
    public ResponseEntity<?> request(UserRequesetDto userRequest) {
        return userService.request(userRequest);
    }

    @Override
    public ResponseEntity<AuthResponse> activeUser(String uuid) {
        try {
            return userService.activateUser(uuid);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.A7));
        }
    }

    @Override
    public ResponseEntity<AuthResponse> resetPassword(ResetPasswordMail email) {
        try {
            userService.recoveryPassword(email.getEmail());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.A2));
        }
    }

    @Override
    public ResponseEntity<AuthResponse> resetPassword(ChangePasswordData changePasswordData) {
        try {
            userService.resetPassword(changePasswordData);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.A2));
        }
    }

    @Override
    public ValidationMessage handleValidationException(MethodArgumentNotValidException ex) {
        return new ValidationMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }

}
