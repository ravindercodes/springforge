package com.ravindercodes.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResponse {

    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private Map<String, String> fieldErrors;
    private String path;

}
