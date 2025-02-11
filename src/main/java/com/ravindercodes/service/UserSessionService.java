package com.ravindercodes.service;

import org.springframework.http.ResponseEntity;

public interface UserSessionService {

    ResponseEntity<?> logoutFromDevice(String devideId);

    ResponseEntity<?> logoutFromAllDevice(long userId);

    ResponseEntity<?> getActiveSessions(long userId);

    ResponseEntity<?> getDisabledSessions(long userId);

}
