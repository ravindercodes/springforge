package com.ravindercodes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravindercodes.constant.CommonConstants;
import com.ravindercodes.dto.response.LoginResponse;
import com.ravindercodes.entity.UserEntity;
import com.ravindercodes.entity.UserSessionEntity;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.repository.UserSessionRepository;
import com.ravindercodes.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtility jwtUtility;
    private final HttpServletRequest request;
    private final UserSessionRepository userSessionRepository;

    public OAuthSuccessHandler(UserRepository userRepository, ModelMapper modelMapper, JwtUtility jwtUtility, HttpServletRequest request, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtUtility = jwtUtility;
        this.request = request;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        LoginResponse loginResponse;

        if (this.userAlreadyExit(oauthUser, provider)) {
            loginResponse = this.getUser(oauthUser, provider);
        } else {
            loginResponse = saveUser(oauthUser, provider);
        }

        Map<String, Object> userDetails = extractUserDetails(loginResponse);
        String userJson = objectMapper.writeValueAsString(userDetails);
        String redirectUrl = UrlUtility.getDashboardUrl() + URLEncoder.encode(userJson, "UTF-8");

        response.sendRedirect(redirectUrl);
    }

    private Map<String, Object> extractUserDetails(LoginResponse loginResponse) {
        Map<String, Object> userDetails = new HashMap<>();

        userDetails.put("username", loginResponse.getUsername());
        userDetails.put("email", loginResponse.getEmail());
        userDetails.put("deviceId", loginResponse.getDeviceId());
        userDetails.put("roles", loginResponse.getRoles());
        userDetails.put("accessToken", loginResponse.getAccessToken());
        userDetails.put("refreshToken", loginResponse.getRefreshToken());

        return userDetails;
    }

    public boolean userAlreadyExit(OAuth2User oauthUser, String provider) {
        if (CommonConstants.GITHUB_LOGIN.equals(provider)) {
            return this.userRepository.existsByEmail(GitHubOAuthUtility.generateEmail(oauthUser.getAttribute("login")));
        }

        if (CommonConstants.GOOGLE_LOGIN.equals(provider)) {
            return this.userRepository.existsByEmail(oauthUser.getAttribute("email"));
        }

        return false;
    }

    public LoginResponse getUser(OAuth2User oauthUser, String provider) {
        if (CommonConstants.GITHUB_LOGIN.equals(provider)) {
            UserEntity user = this.userRepository.findByEmail(GitHubOAuthUtility.generateEmail(oauthUser.getAttribute("login")));
            UserSessionEntity userSession = this.saveUserSession(user);
            LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
            loginResponse.setDeviceId(userSession.getDeviceId());
            loginResponse.setRefreshToken(userSession.getRefreshToken());
            return loginResponse;
        }

        if (CommonConstants.GOOGLE_LOGIN.equals(provider)) {
            UserEntity user = this.userRepository.findByEmail(oauthUser.getAttribute("email"));
            UserSessionEntity userSession = this.saveUserSession(user);
            LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
            loginResponse.setDeviceId(userSession.getDeviceId());
            loginResponse.setRefreshToken(userSession.getRefreshToken());
            return loginResponse;
        }

        return null;
    }

    public LoginResponse saveUser(OAuth2User oauthUser, String provider) {
        String username = UsernameUtility.generateRandomUsername().toLowerCase();
        UserEntity userEntity = new UserEntity();
        if (CommonConstants.GITHUB_LOGIN.equals(provider)) {
            userEntity.setUsername(username);
            userEntity.setEmail(GitHubOAuthUtility.generateEmail(oauthUser.getAttribute("login")));
            userEntity.setPassword(CommonConstants.GITHUB_LOGIN);
            userEntity.setProvider(provider);
            userEntity.setEnabled(true);
        }

        if (CommonConstants.GOOGLE_LOGIN.equals(provider)) {
            userEntity.setUsername(username);
            userEntity.setEmail(oauthUser.getAttribute("email"));
            userEntity.setPassword(CommonConstants.GOOGLE_LOGIN);
            userEntity.setProvider(provider);
            userEntity.setEnabled(true);
        }

        String accessToken = jwtUtility.accessToken(username);

        UserEntity user = this.userRepository.save(userEntity);
        UserSessionEntity savedUserSession = this.saveUserSession(user);

        LoginResponse loginResponse = modelMapper.map(userEntity, LoginResponse.class);
        loginResponse.setDeviceId(savedUserSession.getDeviceId());
        loginResponse.setRefreshToken(savedUserSession.getRefreshToken());

        return loginResponse;
    }

    public UserSessionEntity saveUserSession(UserEntity user) {
        String deviceId = DeviceUtility.generateDeviceId();
        String refreshToken = this.jwtUtility.refreshToken(user.getUsername());

        UserSessionEntity userSessionEntity = new UserSessionEntity(user, deviceId, DeviceUtility.getDeviceName(), IpAddressUtility.getClientIp(request), refreshToken, true);
        return this.userSessionRepository.save(userSessionEntity);
    }

}
