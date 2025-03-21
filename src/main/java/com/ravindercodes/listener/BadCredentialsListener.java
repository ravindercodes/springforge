package com.ravindercodes.listener;

import com.ravindercodes.constant.CommonConstants;
import com.ravindercodes.entity.LoginAttemptEntity;
import com.ravindercodes.service.LoginAttemptService;
import com.ravindercodes.util.IpAddressUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Component
public class BadCredentialsListener {

    private final LoginAttemptService loginAttemptService;

    public BadCredentialsListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent event) {
        String accountIdentifier = event.getAuthentication().getName();
        String ipAddress = getClientIp();

        LoginAttemptEntity loginAttemptEntity = loginAttemptService.findByAccountIdentifier(accountIdentifier)
                .orElseGet(() -> new LoginAttemptEntity(accountIdentifier, ipAddress));

        loginAttemptEntity.setLastAttemptTime(LocalDateTime.now());

        if (loginAttemptEntity.getAttemptCount() >= CommonConstants.MAX_LOGIN_ATTEMPT) {
            loginAttemptEntity.setBlocked(true);
        } else {
            loginAttemptEntity.setAttemptCount(loginAttemptEntity.getAttemptCount() + 1);
        }

        loginAttemptService.failedLoginAttempt(loginAttemptEntity);
    }


    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "UNKNOWN";
        }
        HttpServletRequest request = attrs.getRequest();
        return IpAddressUtility.getClientIp(request);
    }

}
