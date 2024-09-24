package org.example.footballanalyzer.API;

import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping
public interface LoginApi {
    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    ResponseEntity<?> login();
    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody UserDTO user);
}