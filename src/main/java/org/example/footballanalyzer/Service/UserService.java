package org.example.footballanalyzer.Service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.ChangePasswordData;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserRequesetDto;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Data.RoleName;
import org.example.footballanalyzer.Repository.ResetOperationsRepository;
import org.example.footballanalyzer.Repository.RoleRepository;
import org.example.footballanalyzer.Repository.TeamRepository;
import org.example.footballanalyzer.Repository.UserRepository;
import org.example.footballanalyzer.Service.Auth.JwtService;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ResetOperationsRepository resetOperationsRepository;
    private final ResetOperationsService resetOperationsService;
    private final CookieService cookieService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    public ResponseEntity<?> createUser(UserDTO user) {
        Optional<UserEntity> userEntity = userRepository.findByEmailOrLogin(user.getEmail(), user.getLogin());
        if (userEntity.isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.A4));
        }
        Optional<Team> optionalTeam = teamRepository.findByTeamId(user.getTeamId());

        if (optionalTeam.isPresent()) {
            Optional<UserEntity> headCoach = userRepository.findByTeamAndRole_RoleName(optionalTeam.get(), RoleName.COACH);
            if (headCoach.isPresent() && user.getRoleId() == 3){
                return ResponseEntity.badRequest().body(new AuthResponse(Code.R2));
            }
        }

        log.info("Creating user");

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
        UserEntity userEntity = userRepository.findByLoginAndLockAndEnabled(user.getLogin()).orElse(null);
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
        if (request.getCookies() != null) {
            for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
                if (cookie.getName().equals("Authorization")) {
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
                Cookie cookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh, exp), exp);
                response.addCookie(refreshCokie);
                response.addCookie(cookie);
            }
        } else {
            throw new IllegalArgumentException("Token nie może być pusty.");
        }
    }

    public ResponseEntity<?> loginByToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            String refresh = null;
            for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
            String login = jwtService.getSubject(refresh);
            UserEntity user = userRepository.findByLogin(login).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(
                        UserDTO.builder()
                                .login(user.getLogin())
                                .email(user.getEmail())
                                .roleName(user.getRole().getRoleName().name())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .build());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A1));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A3));
        }
    }

    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            return ResponseEntity.ok(new LoginResponse(true));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.ok(new LoginResponse(false));
        }
    }

    public void activateUser(String uuid) {
        UserEntity user = userRepository.findByUuid(uuid).orElse(null);
        if (user != null) {
            user.setLocked(false);
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("Użytkownik nie istnieje");
    }

    public void recoveryPassword(String email) throws RuntimeException {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            ResetOperations resetOperations = resetOperationsService.initResetOperation(user);
            emailService.sedPasswordRecovery(user, resetOperations.getUuid());
        }
        throw new RuntimeException("Użytkownik nie istnieje");
    }

    @Transactional
    public void resetPassword(ChangePasswordData changePasswordData) throws RuntimeException {
        ResetOperations resetOperations = resetOperationsRepository.findByUuid(changePasswordData.getUuid()).orElse(null);
        if (resetOperations != null) {
            UserEntity user = userRepository.findByUuid(changePasswordData.getUuid()).orElse(null);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(changePasswordData.getPassword()));
                userRepository.saveAndFlush(user);
                resetOperationsService.endOperation(changePasswordData.getUuid());
                return;
            }
        }
        throw new RuntimeException("Użytkownik nie istnieje");
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete all cookies");
        Cookie cookie = cookieService.removeCookie(request.getCookies(), "Authorization");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(request.getCookies(), "refresh");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }
}
