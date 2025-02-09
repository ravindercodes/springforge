package com.ravindercodes.dto.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttemptModel {

    private String usernameOrEmail;
    private String ipAddress;
    private int attemptCount;
    private LocalDateTime lastAttemptTime;

}
