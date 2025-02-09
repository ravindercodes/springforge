package com.ravindercodes.exception.custom;

import org.springframework.http.HttpStatus;

public class IpAddressBlockedEx extends CustomEx {
    public IpAddressBlockedEx(String message, HttpStatus status) {
        super(message, status);
    }
}
