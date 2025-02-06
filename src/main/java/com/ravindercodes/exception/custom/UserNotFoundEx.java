package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class UserNotFoundEx extends CustomEx{
    public UserNotFoundEx(String message, HttpStatus status) {
        super(message, status);
    }
}
