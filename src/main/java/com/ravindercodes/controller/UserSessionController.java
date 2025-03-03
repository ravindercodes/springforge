package com.ravindercodes.controller;

import com.ravindercodes.service.UserSessionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-session")
public class UserSessionController {

    @Autowired
    private final UserSessionService userSessionService;

    public UserSessionController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @PostMapping("/logout-from-device")
    public ResponseEntity<?> logoutFromDevice(@RequestParam @NotNull(message = "deviceId is required") final String deviceId) {
        return this.userSessionService.logoutFromDevice(deviceId);
    }

    @PostMapping("/logout-from-all-device")
    public ResponseEntity<?> logoutFromAllDevice(@RequestParam @NotNull(message = "UserId is required") final long userId) {
        return this.userSessionService.logoutFromAllDevice(userId);
    }

    @GetMapping("/active-session")
    public ResponseEntity<?> getActiveSessions(@RequestParam @NotNull(message = "UserId is required") final long userId) {
        return this.userSessionService.getActiveSessions(userId);
    }

    @GetMapping("/disabled-session")
    public ResponseEntity<?> getDisabledSessions(@RequestParam @NotNull(message = "UserId is required") final long userId) {
        return this.userSessionService.getDisabledSessions(userId);
    }
}
