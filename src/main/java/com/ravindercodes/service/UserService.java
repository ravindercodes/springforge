package com.ravindercodes.service;

import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.RefreshTokenRequest;
import com.ravindercodes.dto.request.ResetPasswordRequest;
import com.ravindercodes.dto.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest);

    public ResponseEntity<?> validateToken(String token);

    public ResponseEntity<?> emailVerification(String verificationToken);

    public ResponseEntity<?> forgetPassword(String email);

    public ResponseEntity<?> resetPassword(String verificationToken);

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest);

    public ResponseEntity<?> getRefreshToken(RefreshTokenRequest refreshTokenRequest);

}
