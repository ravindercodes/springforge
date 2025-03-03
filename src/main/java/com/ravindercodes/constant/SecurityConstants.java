package com.ravindercodes.constant;

import java.util.List;

public class SecurityConstants {
    public static final List<String> AUTH_WHITELIST = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/auth/**",
            "/api/test/**",
            "/h2-ui/**",
            "/login/**",
            "/home/**"
    );
}

