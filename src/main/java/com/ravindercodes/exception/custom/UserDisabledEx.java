package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class UserDisabledEx extends CustomEx {
    public UserDisabledEx(String message, HttpStatus status) {
        super(message, status);
    }
}
