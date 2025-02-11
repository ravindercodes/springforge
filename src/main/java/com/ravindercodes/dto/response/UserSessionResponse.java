package com.ravindercodes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionResponse {

    private String deviceId;
    private String deviceName;
    private boolean active;

}
