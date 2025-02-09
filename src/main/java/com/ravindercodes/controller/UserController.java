package com.ravindercodes.controller;

import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.ResetPasswordRequest;
import com.ravindercodes.dto.request.SignupRequest;
import com.ravindercodes.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return this.userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return this.userService.registerUser(signUpRequest);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam @NotBlank(message = "Token is required") String token) {
        return this.userService.validateToken(token);
    }

    @GetMapping("/email-verification")
    public ResponseEntity<?> emailVerification(@RequestParam @NotBlank(message = "Verification Token is required") String verificationToken) {
        return this.userService.emailVerification(verificationToken);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam @Email @NotBlank(message = "Email is required") String email) {
        return this.userService.forgetPassword(email);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam @NotBlank(message = "Verification Token is required") String verificationToken) {
        return this.userService.resetPassword(verificationToken);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return this.userService.resetPassword(resetPasswordRequest);
    }
}
