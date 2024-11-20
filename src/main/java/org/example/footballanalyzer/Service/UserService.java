package org.example.footballanalyzer.Service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.ChangePasswordData;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.*;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Data.RoleName;
import org.example.footballanalyzer.Repository.*;
import org.example.footballanalyzer.Service.Auth.JwtService;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    private final HttpServletRequest request;
    private final UserRequestRepository userRequestRepository;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    public ResponseEntity<?> createUser(UserDTO user) {
        Optional<UserEntity> userEntity = userRepository.findFirstByEmailOrLogin(user.getEmail(), user.getLogin());
        if (userEntity.isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.A4));
        }
        Optional<Team> optionalTeam = teamRepository.findById(user.getTeamId() == null ? -2 : user.getTeamId());

        if (optionalTeam.isPresent()) {
            Optional<Role> role = roleRepository.findByRoleName(RoleName.TRENER);
            if (role.isPresent()) {
                Optional<UserEntity> headCoach = userRepository.findFirstByTeamAndRole(optionalTeam.get(), role.get());
                if (headCoach.isPresent() && user.getRoleId() == role.get().getId()) {
                    return ResponseEntity.badRequest().body(new AuthResponse(Code.R2));
                }
            }
        }

        log.info("Creating user");

        return dataUtil.saveUserToDb(user, optionalTeam);
    }

    public List<UserEntityEditData> getAllUsers() {
        return userRepository.findAll().stream().map(user -> UserEntityEditData.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .roleId(user.getRole().getId())
                .teamName(user.getTeam() == null ? null : user.getTeam().getName())
                .teamId(user.getTeam() == null ? null : user.getTeam().getId())
                .roleName(user.getRole().getRoleName().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .hasPdf(user.getCoachConfirmPdf() != null)
                .build()).toList();
    }

    public ResponseEntity<?> getRoles(boolean isAdmin) {
        RoleName role = RoleName.ADMIN;
        if (isAdmin) {
            return ResponseEntity.ok().body(roleRepository.findAll().stream().toList());
        }
        return ResponseEntity.ok().body(roleRepository.findAllByRoleNameNot(role));
    }

    public ResponseEntity<?> request(UserRequestDto userRequest) {
        log.info("Saving new request: {}", userRequest.getRequestType());
        String requestData = userRequest.getRequestData().toString();
        String login = userRequest.getLogin();
        if (login == null) {
            UserEntity user = findUser();
            login = user.getLogin();
        }
        return dataUtil.saveNewRequest(userRequest, requestData, login);
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
                                .teamLogo(userEntity.getTeam() == null ? null : userEntity.getTeam().getLogo())
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
                                .teamLogo(user.getTeam() == null ? null : user.getTeam().getLogo())
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
            userRepository.unlockUser(uuid, false);
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


    public ResponseEntity<?> getRole() {
        UserEntity user = findUser();

        RoleName roleName = user.getRole().getRoleName();

        return ResponseEntity.ok(roleName);
    }

    @Transactional
    public UserEntityEditData updateUser(Long id, UserEntityEditData user) throws FileAlreadyExistsException {
        log.info("Updating user with id: {}", id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        log.info(user.toString());

        if (user.getFirstName() != null) {
            userEntity.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userEntity.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            if (userRepository.findByEmail(user.getEmail()).isPresent() && !user.getEmail().equals(userEntity.getEmail())) {
                throw new FileAlreadyExistsException("Email already exists");
            }
            userEntity.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            if (userRepository.findByLogin(user.getLogin()).isPresent() && !user.getLogin().equals(userEntity.getLogin())) {
                throw new FileAlreadyExistsException("Login already exists");
            }
            userEntity.setLogin(user.getLogin());
        }
        if (user.getRoleId() != null) {
            roleRepository.findById(user.getRoleId()).ifPresent(userEntity::setRole);
        }
        if (user.getTeamId() != null) {
            if (user.getTeamId() == -1) {
               userEntity.setTeam(null);
            } else {
                Team team = teamRepository.findById(user.getTeamId()).orElse(null);
                if (team != null) {
                    Optional<UserEntity> otherCoach = userRepository.findFirstByTeamAndRole_RoleName(team, RoleName.TRENER);
                    if (otherCoach.isPresent() && userEntity.getRole().getRoleName().name().equals("TRENER") && !Objects.equals(otherCoach.get().getId(), userEntity.getId())) {
                        throw new ExceptionInInitializerError("Team already has a coach");
                    }
                    userEntity.setTeam(team);
                }
            }
        }
        if (user.getPassword() != "" && user.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userEntity);
        return getUserEntityEditData(userEntity);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        userRequestRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }

    public UserEntityEditData updateUserTeam(Long id, Long teamId) throws FileAlreadyExistsException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new FileAlreadyExistsException("Not found"));
        Optional<UserEntity> otherCoach = userRepository.findFirstByTeamAndRole_RoleName(team, RoleName.TRENER);
        if (otherCoach.isPresent() && userEntity.getRole().getRoleName().name().equals("TRENER")) {
            throw new ExceptionInInitializerError("Team already has a coach");
        }

        userRepository.updateTeam(id, teamId);
        return getUserEntityEditData(userEntity);
    }

    private UserEntityEditData getUserEntityEditData(UserEntity userEntity) {
        return UserEntityEditData.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .email(userEntity.getEmail())
                .roleId(userEntity.getRole().getId())
                .teamName(userEntity.getTeam() == null ? null : userEntity.getTeam().getName())
                .teamId(userEntity.getTeam() == null ? null : userEntity.getTeam().getId())
                .roleName(userEntity.getRole().getRoleName().name())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    public void unlockUser(Long id, boolean unlock) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        if (userEntity.isLocked() != unlock) {
            userRepository.unlockUser(userEntity.getUuid(), unlock);
        }
    }

    public List<UserRequestDto> getAllRequests(String sortBy, String sortDirection) {
        List<UserRequest> userRequests;
        if (sortBy == null && sortDirection == null) {
            userRequests = userRequestRepository.findAll();
        } else {
            if (Objects.equals(sortBy, "login")) {
                sortBy = "user.login";
            }
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            userRequests = userRequestRepository.findAll(sort);
        }
        if (userRequests.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        return userRequests.stream().map(userRequest -> UserRequestDto.builder()
                .id(userRequest.getId())
                .userId(userRequest.getUser().getId())
                .login(userRequest.getUser().getLogin())
                .createDate(userRequest.getCreateDate())
                .requestType(userRequest.getRequestType())
                .requestData(userRequest.getRequestData())
                .requestStatus(userRequest.getRequestStatus())
                .build()).toList();
    }

    @Transactional
    public void setAsResolved(Long id, String status) {
        UserRequest userRequest = userRequestRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        UserRequest.RequestStatus requestStatus = UserRequest.RequestStatus.valueOf(status);
        userRequest.setRequestStatus(requestStatus);
        userRequestRepository.save(userRequest);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredRequests() {
        userRequestRepository.deleteByRequestStatus(UserRequest.RequestStatus.ZAMKNIĘTE);
    }

    public ResponseEntity<?> downloadConfirmationPdf(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        try {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=confirmation.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new ByteArrayInputStream(user.getCoachConfirmPdf())));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Not found");
        }
    }

    public void deleteRequest(Long id) {
        userRequestRepository.deleteById(id);
    }

    public ResponseEntity<?> uploadConfirm(String userLogin, MultipartFile file) throws IOException {
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.F1));
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.F2));
        }
        UserEntity user = userRepository.findByLogin(userLogin).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        try {
            user.setCoachConfirmPdf(file.getBytes());
            userRepository.uploadFile(user.getId(), file.getBytes());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getStaff() {
        UserEntity user = findUser();
        List<UserEntity> users = userRepository.findAllByTeam(user.getTeam());
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        users = users.stream().filter(u -> !Objects.equals(u.getId(), user.getId())).toList();
        List<UserStaff> userEntityDtos = users.stream().map(u -> UserStaff.builder()
                .id(u.getId())
                .login(u.getLogin())
                .roleName(u.getRole().getRoleName().name())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .build()).toList();
        return ResponseEntity.ok(userEntityDtos);
    }

    @Transactional
    public ResponseEntity<?> setAsCoach(Long id) {
        UserEntity user = findUser();
        UserEntity newCoach = userRepository.findById(id).orElse(null);
        if (newCoach == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.ERROR));
        }
        Role coachRole = roleRepository.findByRoleName(RoleName.TRENER).orElse(null);
        Role analitykRole = roleRepository.findByRoleName(RoleName.ANALITYK).orElse(null);

        if (coachRole == null || analitykRole == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.ERROR));
        }

        userRepository.updateRole(user.getId(), analitykRole.getId());
        userRepository.updateRole(id, coachRole.getId());
        return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
    }

    public ResponseEntity<?> removeFromTeam(Long id) {
        UserEntity deleteCoach = userRepository.findById(id).orElse(null);
        if (deleteCoach == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.ERROR));
        }
        userRepository.updateTeam(id, null);
        return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
    }

    public ResponseEntity<?> getRequests() {
        UserEntity user = findUser();
        List<UserRequest> userRequests = userRequestRepository.findAllByUser(user);
        if (userRequests.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        List<UserRequestDto> userRequestDtos = userRequests.stream().map(userRequest -> UserRequestDto.builder()
                .id(userRequest.getId())
                .userId(userRequest.getUser().getId())
                .login(userRequest.getUser().getLogin())
                .createDate(userRequest.getCreateDate())
                .requestType(userRequest.getRequestType())
                .requestData(userRequest.getRequestData())
                .requestStatus(userRequest.getRequestStatus())
                .build()).toList();
        return ResponseEntity.ok(userRequestDtos);
    }

    private UserEntity findUser() {
        String username = request.getUserPrincipal().getName();
        if (username == null) {
            throw new UsernameNotFoundException("Not found");
        }
        UserEntity user = userRepository.findByLogin(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Not found");
        }
        return user;
    }

    public ResponseEntity<?> getUser() {
        UserEntity user = findUser();
        UserEntityEditData userEntityDto = UserEntityEditData.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .roleId(user.getRole().getId())
                .teamName(user.getTeam() == null ? null : user.getTeam().getName())
                .teamId(user.getTeam() == null ? null : user.getTeam().getId())
                .roleName(user.getRole().getRoleName().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .hasPdf(user.getCoachConfirmPdf() != null)
                .build();
        return ResponseEntity.ok(userEntityDto);
    }
}
