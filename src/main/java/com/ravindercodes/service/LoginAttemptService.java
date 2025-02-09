package com.ravindercodes.service;

import com.ravindercodes.entity.LoginAttemptEntity;

import java.util.Optional;

public interface LoginAttemptService {

    LoginAttemptEntity failedLoginAttempt(LoginAttemptEntity loginAttemptEntity);

    boolean isIpBlocked(String ipAddress);

    boolean resetAttempts(long id);

    long getIpUnlockTime(String ipAddress);

    LoginAttemptEntity findById(long id);

    LoginAttemptEntity findByIpAddress(String ipAddress);

    Optional<LoginAttemptEntity> findByAccountIdentifier(String accountIdentifier);

}
