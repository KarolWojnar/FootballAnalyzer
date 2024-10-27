package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/users")
@Description("API for managing users")
public interface UserApi {
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<List<UserDTO>> getAllUsers();

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserDTO user);

    @PostMapping("/authenticate")
    ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest);
}
