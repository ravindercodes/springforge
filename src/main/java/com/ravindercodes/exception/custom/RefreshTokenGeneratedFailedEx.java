package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class RefreshTokenGeneratedFailedEx extends CustomEx {
    public RefreshTokenGeneratedFailedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
