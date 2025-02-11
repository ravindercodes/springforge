package com.ravindercodes.util;

import com.ravindercodes.constant.CommonConstants;
import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.exception.custom.JwtGenerationEx;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtility {

    @Value("${jwt.accessTokenExpireMs}")
    private int accessTokenExpireMs;

    @Value("${jwt.refreshTokenExpireMs}")
    private int refreshTokenExpireMs;

    @Value("${jwt.emailTokenExpireMs}")
    private int emailTokenExpireMs;

    private PublicKey cachedPublicKey = null;
    private PrivateKey cachedPrivateKey = null;

    private PrivateKey getPrivateKey() throws Exception {
        if (cachedPrivateKey != null) {
            return cachedPrivateKey;
        }
        String key = readKeyFromFile(CommonConstants.PRIVATE_KEY_PATH);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.cachedPrivateKey = kf.generatePrivate(spec);
        return cachedPrivateKey;
    }

    private PublicKey getPublicKey() throws Exception {
        if (cachedPublicKey != null) {
            return cachedPublicKey;
        }
        String key = readKeyFromFile(CommonConstants.PUBLIC_KEY_PATH);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.cachedPublicKey = kf.generatePublic(spec);
        return cachedPublicKey;
    }


    private String readKeyFromFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String key = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            return key;
        }
    }

    public String accessToken(String subject) {
        return this.generateToken(subject, new Date(System.currentTimeMillis() + accessTokenExpireMs));
    }

    public String refreshToken(String subject) {
        return this.generateToken(subject, new Date(System.currentTimeMillis() + refreshTokenExpireMs));
    }

    public String emailVerificationToken(String subject) {
        return this.generateToken(subject, new Date(System.currentTimeMillis() + emailTokenExpireMs));
    }

    public String generateToken(String subject, Date date) {
        try {
            return Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(new Date())
                    .setExpiration(date)
                    .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                    .compact();
        } catch (Exception e) {
            log.error("Unexpected error during JWT generation for subject: {}. Exception: {}", subject, e.getMessage(), e);
            throw new JwtGenerationEx(MessagesConstants.JWT_GENERATION_EX, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Claims extractClaims(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey((PublicKey) getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse token", e);
        }
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> validateToken(String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .parseClaimsJws(token)
                    .getBody();

            response.put("valid", true);
            response.put("subject", claims.getSubject());
            response.put("expiration", claims.getExpiration());
            response.put("message", "Token is valid");

        } catch (ExpiredJwtException e) {
            response.put("valid", false);
            response.put("message", "Token has expired");
        } catch (UnsupportedJwtException e) {
            response.put("valid", false);
            response.put("message", "Unsupported JWT token");
        } catch (MalformedJwtException e) {
            response.put("valid", false);
            response.put("message", "Malformed JWT token");
        } catch (SignatureException e) {
            response.put("valid", false);
            response.put("message", "Invalid JWT signature");
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Token validation failed");
        }

        return response;
    }
}
