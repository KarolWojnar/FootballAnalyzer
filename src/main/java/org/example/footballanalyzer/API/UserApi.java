package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
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
