package com.ravindercodes.service;

import org.springframework.http.ResponseEntity;

public interface UserSessionService {

    ResponseEntity<?> logoutFromDevice(String deviceId);

    ResponseEntity<?> logoutFromAllDevice(long userId);

    ResponseEntity<?> getActiveSessions(long userId, int page, int size, String sortBy, String sortDir);

    ResponseEntity<?> getDisabledSessions(long userId, int page, int size, String sortBy, String sortDir);

}
