package com.ravindercodes.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtility {

    private static String BASE_URL;
    @Value("${base.url}")
    private String baseUrl;

    public static String getVerificationUrl(String token) {
        return BASE_URL + "api/auth/email-verification?verificationToken=" + token;
    }

    @PostConstruct
    private void init() {
        BASE_URL = baseUrl;
    }
}

