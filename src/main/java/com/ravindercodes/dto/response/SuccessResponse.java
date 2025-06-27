package com.ravindercodes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private int status;
    private String message;
    private T data;
    private Object metaData;

    public SuccessResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> SuccessResponse<T> success(String message, T data, Object metaData) {
        return new SuccessResponse<>(200, message, data, metaData);
    }

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>(200, message, data);
    }
}

