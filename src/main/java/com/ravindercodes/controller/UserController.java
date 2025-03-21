package com.ravindercodes.controller;

import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.RefreshTokenRequest;
import com.ravindercodes.dto.request.ResetPasswordRequest;
import com.ravindercodes.dto.request.SignupRequest;
import com.ravindercodes.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {
        return this.userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody final SignupRequest signUpRequest) {
        return this.userService.registerUser(signUpRequest);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam @NotBlank(message = "Token is required") final String token) {
        return this.userService.validateToken(token);
    }

    @GetMapping("/email-verification")
    public ResponseEntity<?> emailVerification(@RequestParam @NotBlank(message = "Verification Token is required") final String verificationToken) {
        return this.userService.emailVerification(verificationToken);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam @Email @NotBlank(message = "Email is required") final String email) {
        return this.userService.forgetPassword(email);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam @NotBlank(message = "Verification Token is required") final String verificationToken) {
        return this.userService.resetPassword(verificationToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody final ResetPasswordRequest resetPasswordRequest) {
        return this.userService.resetPassword(resetPasswordRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        return this.userService.getRefreshToken(refreshTokenRequest);
    }
}
