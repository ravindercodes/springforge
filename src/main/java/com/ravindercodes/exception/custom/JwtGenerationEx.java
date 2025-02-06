package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class JwtGenerationEx extends CustomEx{
    public JwtGenerationEx(String message, HttpStatus status) {
        super(message, status);
    }
}
