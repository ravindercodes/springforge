package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class ResetPasswordFailedEx extends CustomEx {
    public ResetPasswordFailedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
