package com.ravindercodes.service;

import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest);

    public ResponseEntity<?> validateToken(String token);
    
}
