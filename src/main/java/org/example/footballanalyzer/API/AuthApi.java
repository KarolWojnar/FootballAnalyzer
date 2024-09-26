package org.example.footballanalyzer.API;

import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthApi {
    @GetMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);
    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody UserDTO user);
}