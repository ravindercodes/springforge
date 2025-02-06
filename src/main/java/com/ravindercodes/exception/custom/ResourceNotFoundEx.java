package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundEx extends CustomEx {
    public ResourceNotFoundEx(String message, HttpStatus status) {
        super(message, status);
    }
}
