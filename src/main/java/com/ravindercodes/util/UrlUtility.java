package com.ravindercodes.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtility {

    private static String backendUrl;
    private static String frontendUrl;

    @Value("${base.backend.url}")
    private String baseBackendUrl;

    @Value("${base.frontend.url}")
    private String baseFrontendUrl;

    public static String getVerificationUrl(String verificationToken) {
        return backendUrl + "api/auth/email-verification?verificationToken=" + verificationToken;
    }

    public static String getResetPasswordUrl(String verificationToken) {
        return frontendUrl + "api/auth/reset-password?verificationToken=" + verificationToken;
        //return frontendUrl + "reset-password?token=" + token;
    }

    @PostConstruct
    private void init() {
        backendUrl = baseBackendUrl;
        frontendUrl = baseFrontendUrl;
    }
}


