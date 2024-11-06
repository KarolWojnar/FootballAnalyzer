package org.example.footballanalyzer.Service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.ChangePasswordData;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserLoginData;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        Optional<UserEntity> userEntity = userRepository.findFirstByEmailOrLogin(user.getEmail(), user.getLogin());
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

    public ResponseEntity<?> login(UserLoginData user, HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity = userRepository.findByLoginAndLockAndEnabled(user.getLogin()).orElse(null);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.A2));
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );
            if (authentication.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie("refresh", jwtService.generateToken(user.getLogin(), refreshExp), refreshExp);
                Cookie cookie = cookieService.generateCookie("Authorization", jwtService.generateToken(user.getLogin(), exp), exp);

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

    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;

        if (request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
        }

        if (token == null || refresh == null) {
            log.info("Can't login because token or refresh token is empty");
            throw new IllegalArgumentException("Token or refresh token can't be null");
        }

        try {
            jwtService.validateTokenExp(token);
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            try {
                jwtService.validateTokenExp(refresh);
                Cookie refreshCookie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh, refreshExp), refreshExp);
                Cookie authCookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh, exp), exp);
                response.addCookie(authCookie);
                response.addCookie(refreshCookie);
            } catch (ExpiredJwtException | IllegalArgumentException refreshEx) {
                log.error("Both tokens are expired or invalid");
                throw new IllegalArgumentException("Both token and refresh token are invalid");
            }
        }
    }

    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            return ResponseEntity.ok(new LoginResponse(true));
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return ResponseEntity.ok(new LoginResponse(false));
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

    public ResponseEntity<AuthResponse> activateUser(String uuid) {
        UserEntity user = userRepository.findByUuid(uuid).orElse(null);
        if (user != null) {
            userRepository.unlockUser(uuid);
            log.info("User id: {} has been activated", user.getId());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        }
        return ResponseEntity.status(400).body(new AuthResponse(Code.A9));
    }

    public void recoveryPassword(String email) throws IOException {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            log.info("User id: {} has been sent reset password", user.getId());
            ResetOperations resetOperations = resetOperationsService.initResetOperation(user);
            emailService.sedPasswordRecovery(user, resetOperations.getUuid());
        } else {
            throw new IOException("Użytkownik nie istnieje");
        }
    }

    @Transactional
    public void resetPassword(ChangePasswordData changePasswordData) throws RuntimeException {
        ResetOperations resetOperations = resetOperationsRepository.findByUuid(changePasswordData.getUuid()).orElse(null);
        if (resetOperations != null) {
            UserEntity user = userRepository.findByUuid(resetOperations.getUser().getUuid()).orElse(null);
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
        Cookie authCookie = cookieService.removeCookie(request.getCookies(), "Authorization");
        if (authCookie != null) {
            response.addCookie(authCookie);
        }

        Cookie refreshCookie = cookieService.removeCookie(request.getCookies(), "refresh");
        if (refreshCookie != null) {
            response.addCookie(refreshCookie);
        }

        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }


}
