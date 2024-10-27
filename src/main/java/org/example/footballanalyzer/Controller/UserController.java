package org.example.footballanalyzer.Controller;

import org.example.footballanalyzer.API.UserApi;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.example.footballanalyzer.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<?> createUser(UserDTO user) {
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @Override
    public ResponseEntity<?> authenticate(AuthRequest authRequest) {
        return ResponseEntity.ok().body(userService.getAuthority(authRequest));
    }

}
