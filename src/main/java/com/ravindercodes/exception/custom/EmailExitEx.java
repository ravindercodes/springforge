package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class EmailExitEx extends CustomEx {
    public EmailExitEx(String message, HttpStatus status) {
        super(message, status);
    }
}
