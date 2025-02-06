package com.ravindercodes.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
}

