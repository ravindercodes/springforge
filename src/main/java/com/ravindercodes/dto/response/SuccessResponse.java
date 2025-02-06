package com.ravindercodes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>("success", message, data);
    }
}
