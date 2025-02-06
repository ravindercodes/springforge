package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class UsernameExitEx extends CustomEx {
    public UsernameExitEx(String message, HttpStatus status) {
        super(message, status);
    }
}
