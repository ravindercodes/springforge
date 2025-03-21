package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class AuthorizationDeniedEx extends CustomEx {
    public AuthorizationDeniedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
