package org.example.footballanalyzer.Controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.UserApi;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserRequesetDto;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.example.footballanalyzer.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<?> createUser(UserDTO user) {
        return userService.createUser(user);
    }

    @Override
    public ResponseEntity<?> login(UserDTO user, HttpServletResponse response) {
        return userService.login(user, response);
    }

    @Override
    public ResponseEntity<?> authenticate(AuthRequest authRequest) {
        return userService.getAuthority(authRequest);
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

}
