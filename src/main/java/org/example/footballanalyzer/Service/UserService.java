package org.example.footballanalyzer.Service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserRequesetDto;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Data.RoleName;
import org.example.footballanalyzer.Repository.RoleRepository;
import org.example.footballanalyzer.Repository.TeamRepository;
import org.example.footballanalyzer.Repository.UserRepository;
import org.example.footballanalyzer.Service.Auth.AuthRequest;
import org.example.footballanalyzer.Service.Auth.JwtService;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TeamRepository teamRepository;
    private final RoleRepository roleRepository;
    private final DataUtil dataUtil;
    private final CookieService cookieService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    public ResponseEntity<?> createUser(UserDTO user) {
        Optional<UserEntity> userEntity = userRepository.findByEmailOrLogin(user.getEmail(), user.getLogin());
        if (userEntity.isPresent()) {
            return ResponseEntity.badRequest().body("Login/email już zajęty.");
        }
        log.info("Creating user");
        Optional<Team> optionalTeam = teamRepository.findByTeamId(user.getTeamId());

        return dataUtil.saveUserToDb(user, optionalTeam);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
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
                return ResponseEntity.ok().body(jwtService.generateToken(authRequest.getUsername(), exp));
            } else {
                return ResponseEntity.badRequest().body("Invalid user credentials");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getRoles() {
        RoleName role = RoleName.ADMIN;
        return ResponseEntity.ok().body(roleRepository.findAllByRoleNameNot(role));
    }

    public ResponseEntity<?> request(UserRequesetDto userRequest) {
        log.info("Saving new request: {}", userRequest.getRequestType());
        String requestData = userRequest.getRequestData().toString();
        return dataUtil.saveNewRequest(userRequest, requestData);
    }

    public ResponseEntity<?> login(UserDTO user, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByLogin(user.getLogin()).orElse(null);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.A2));
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );
            if (authentication.isAuthenticated()) {
                Cookie cookie = cookieService.generateCookie("token", jwtService.generateToken(user.getLogin(), exp), exp);
                Cookie refresh = cookieService.generateCookie("refresh", jwtService.generateToken(user.getLogin(), refreshExp), refreshExp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                        UserDTO.builder()
                                .login(userEntity.getLogin())
                                .email(userEntity.getEmail())
                                .roleName(userEntity.getRole().getRoleName().name())
                                .firstName(userEntity.getFirstName())
                                .lastName(userEntity.getLastName())
                                .build());
            } else {
                return ResponseEntity.badRequest().body(new AuthResponse(Code.A1));
            }
        }
    }

    public void validateToken(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        String refresh = null;
        for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            } else if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        try {
            jwtService.validateTokenExp(token);
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            jwtService.validateTokenExp(refresh);
            Cookie refreshCokie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh, refreshExp), refreshExp);
            Cookie cookie = cookieService.generateCookie("token", jwtService.refreshToken(refresh, exp), exp);
            response.addCookie(refreshCokie);
            response.addCookie(cookie);
        }
    }
}
