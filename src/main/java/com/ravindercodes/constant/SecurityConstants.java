package com.ravindercodes.constant;

import java.util.List;

public class SecurityConstants {
    public static final List<String> AUTH_WHITELIST = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/auth/**",
            "/h2-ui/**",
            "/api/v1/login/**",
            "/api/v1/home/**"
    );
}

