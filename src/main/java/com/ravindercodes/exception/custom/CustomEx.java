package com.ravindercodes.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomEx extends RuntimeException {
    private final HttpStatus status;

    public CustomEx(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
