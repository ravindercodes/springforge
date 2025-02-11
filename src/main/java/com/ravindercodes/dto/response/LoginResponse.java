package com.ravindercodes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String username;
    private String email;
    private String deviceId;
    private List<String> roles;
    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;

    public LoginResponse(String username, String email, String deviceId, List<String> roles, String accessToken, String refreshToken) {
        this.username = username;
        this.email = email;
        this.deviceId = deviceId;
        this.roles = roles;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
