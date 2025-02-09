package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class EmailVerificationFailedEx extends CustomEx {
    public EmailVerificationFailedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
