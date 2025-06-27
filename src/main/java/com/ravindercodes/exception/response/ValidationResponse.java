package com.ravindercodes.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResponse {

    private long timestamp;
    private int status;
    private String exception;
    private String message;
    private Map<String, String> error;
    private String path;

}
