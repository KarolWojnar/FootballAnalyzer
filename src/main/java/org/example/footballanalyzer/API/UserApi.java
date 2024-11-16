package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.example.footballanalyzer.Data.ChangePasswordData;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserLoginData;
import org.example.footballanalyzer.Data.Dto.UserRequestDto;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Data.ResetPasswordMail;
import org.example.footballanalyzer.Data.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
@Description("API for managing users")
public interface UserApi {
    @PostMapping(value = "/register")
    ResponseEntity<?> createUser(@RequestBody UserDTO user);

    @PatchMapping(value = "/uploadConfirmFile/{login}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> uploadConfirm(@PathVariable String login, @RequestParam("file") MultipartFile file);

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody UserLoginData user, HttpServletResponse response);

    @GetMapping("/role")
    ResponseEntity<?> getRole();

    @GetMapping("/auto-login")
    ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response);

    @GetMapping("/logged-in")
    ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response);

    @GetMapping("/validate")
    ResponseEntity<AuthResponse> validate(HttpServletRequest request, HttpServletResponse response);

    @GetMapping("/logout")
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);
    @PostMapping("/requests")
    ResponseEntity<?> request(@RequestBody UserRequestDto userRequest);

    @GetMapping("/activate")
    ResponseEntity<AuthResponse> activeUser(@RequestParam String uuid);

    @PostMapping("/reset-password")
    ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordMail email);

    @PatchMapping("/reset-password")
    ResponseEntity<AuthResponse> resetPassword(@RequestBody ChangePasswordData changePasswordData);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ValidationMessage handleValidationException(MethodArgumentNotValidException ex);
}
