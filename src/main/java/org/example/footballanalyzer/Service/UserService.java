package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Entity.Role;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Repository.RoleRepository;
import org.example.footballanalyzer.Repository.TeamRepository;
import org.example.footballanalyzer.Repository.UserRepository;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.example.footballanalyzer.Service.Auth.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TeamRepository teamRepository;
    private final RoleRepository roleRepository;

    public ResponseEntity<?> createUser(UserDTO user) {
        Optional<UserEntity> userEntity = userRepository.findByEmailOrLogin(user.getEmail(), user.getLogin());
        if (userEntity.isPresent()) {
            return ResponseEntity.badRequest().body("User already exist");
        }
        log.info("Creating user: {}", user.getLogin());
        Team team = teamRepository.findByTeamId(user.getTeamId()).orElseThrow();
        Role role = roleRepository.findById(3L).orElseThrow();
        UserEntity newUser = UserEntity.builder()
                .login(user.getLogin())
                .team(team)
                .role(role)
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
        UserDTO newUserDto = UserDTO.builder()
                .id(userRepository.save(newUser).getId())
                .login(newUser.getLogin())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .build();
        return ResponseEntity.ok().body(newUserDto);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .teamId(user.getTeam().getTeamId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build()).toList();
    }

    public ResponseEntity<?> getAuthority(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok().body(jwtService.generateToken(authRequest.getUsername()));
            } else {
                return ResponseEntity.badRequest().body("Invalid user credentials");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }
}
