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
    private List<String> roles;
    private String type = "Bearer";
    private String accessToken;

    public LoginResponse(String username, String email, List<String> roles, String accessToken) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.accessToken = accessToken;
    }
}
