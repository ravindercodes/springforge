package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class EmailSendFailedEx extends CustomEx {
    public EmailSendFailedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
