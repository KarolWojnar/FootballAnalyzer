package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserRequesetDto;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody UserDTO user, HttpServletResponse response);

    @PostMapping("/authenticate")
    ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest);

    @GetMapping("/validate")
    ResponseEntity<AuthResponse> validate(HttpServletRequest request, HttpServletResponse response);
    @PostMapping("/requests")
    ResponseEntity<?> request(@RequestBody UserRequesetDto userRequest);
}
