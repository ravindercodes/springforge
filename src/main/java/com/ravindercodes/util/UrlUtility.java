package com.ravindercodes.util;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class UrlUtility {

    @Value("${base.url}")
    private String baseUrl;

    private static String BASE_URL;

    @PostConstruct
    private void init() {
        BASE_URL = baseUrl;
    }

    public static String getVerificationUrl(String token) {
        return BASE_URL + "api/auth/email-verification?verificationToken=" + token;
    }
}

