package com.ravindercodes.controller;

import com.ravindercodes.service.UserSessionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-session")
public class UserSessionController {

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
    public ResponseEntity<?> getActiveSessions(@RequestParam @NotNull(message = "UserId is required") final long userId,
                                               @RequestParam(defaultValue = "0") final int page,
                                               @RequestParam(defaultValue = "10") final int size,
                                               @RequestParam(defaultValue = "id") final String sortBy,
                                               @RequestParam(defaultValue = "asc") final String sortDir) {
        return this.userSessionService.getActiveSessions(userId, page, size, sortBy, sortDir);
    }

    @GetMapping("/disabled-session")
    public ResponseEntity<?> getDisabledSessions(@RequestParam @NotNull(message = "UserId is required") final long userId,
                                                 @RequestParam(defaultValue = "0") final int page,
                                                 @RequestParam(defaultValue = "10") final int size,
                                                 @RequestParam(defaultValue = "id") final String sortBy,
                                                 @RequestParam(defaultValue = "asc") final String sortDir) {
        return this.userSessionService.getDisabledSessions(userId, page, size, sortBy, sortDir);
    }
}
