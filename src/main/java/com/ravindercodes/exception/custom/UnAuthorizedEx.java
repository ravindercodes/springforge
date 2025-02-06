package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class UnAuthorizedEx extends CustomEx {
    public UnAuthorizedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
