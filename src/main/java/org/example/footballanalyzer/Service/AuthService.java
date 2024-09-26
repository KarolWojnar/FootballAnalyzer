package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Entity.Role;
import org.example.footballanalyzer.Data.Entity.User;
import org.example.footballanalyzer.Data.LoginRequest;
import org.example.footballanalyzer.Repository.RoleRepository;
import org.example.footballanalyzer.Repository.UserRepository;
import org.example.footballanalyzer.Service.Util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RoleRepository roleRepository;

    public ResponseEntity<?> generateToken(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password())
        );
        final var userDetails = userDetailsService.loadUserByUsername(loginRequest.login());
        log.info("User details: {}", userDetails);
        return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
    }

    public ResponseEntity<String> register(UserDTO user) {
        Optional<User> userOptional = userRepository.findByLoginOrEmail(user.getLogin(), user.getEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        Role role = roleRepository.findByRoleName(user.getRole()).orElseThrow(
                () -> new RuntimeException("Role not found")
        );

        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(hashPassword(user.getPassword()));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setRole(role);
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
