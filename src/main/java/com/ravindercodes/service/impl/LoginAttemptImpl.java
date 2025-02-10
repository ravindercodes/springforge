package com.ravindercodes.service.impl;

import com.ravindercodes.constant.CommonConstants;
import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.entity.LoginAttemptEntity;
import com.ravindercodes.exception.custom.ResourceNotFoundEx;
import com.ravindercodes.repository.LoginAttemptRepository;
import com.ravindercodes.service.LoginAttemptService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginAttemptImpl implements LoginAttemptService {

    @Autowired
    private final LoginAttemptRepository loginAttemptRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public LoginAttemptImpl(LoginAttemptRepository loginAttemptRepository, ModelMapper modelMapper) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LoginAttemptEntity failedLoginAttempt(LoginAttemptEntity loginAttemptEntity) {
        return this.loginAttemptRepository.save(loginAttemptEntity);
    }

    @Override
    public boolean isIpBlocked(String ipAddress) {
        LoginAttemptEntity loginAttemptEntity = this.findByIpAddress(ipAddress);
        if (loginAttemptEntity == null) {
            return false;
        }
        if (loginAttemptEntity.isBlocked()) {
            LocalDateTime unblockTime = loginAttemptEntity.getLastAttemptTime().plusMinutes(CommonConstants.IP_BLOCK_TIME_MINUTES);
            if (LocalDateTime.now().isAfter(unblockTime)) {
                this.resetAttempts(loginAttemptEntity.getId());
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean resetAttempts(long id) {
        LoginAttemptEntity loginAttemptEntity = this.findById(id);
        loginAttemptEntity.setAttemptCount(0);
        this.loginAttemptRepository.save(loginAttemptEntity);
        return true;
    }

    @Override
    public long getIpUnlockTime(String ipAddress) {
        LoginAttemptEntity loginAttemptEntity = this.findByIpAddress(ipAddress);
        LocalDateTime unlockTime = loginAttemptEntity.getLastAttemptTime()
                .plusMinutes(CommonConstants.IP_BLOCK_TIME_MINUTES);

        long minutesLeft = Duration.between(LocalDateTime.now(), unlockTime).toMinutes();
        return Math.max(minutesLeft, 0);
    }

    @Override
    public LoginAttemptEntity findById(long id) {
        return this.loginAttemptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundEx(MessagesConstants.RECORD_NOT_FOUND, HttpStatus.BAD_REQUEST));
    }

    @Override
    public LoginAttemptEntity findByIpAddress(String ipAddress) {
        return this.loginAttemptRepository.findByIpAddress(ipAddress);
    }

    @Override
    public Optional<LoginAttemptEntity> findByAccountIdentifier(String accountIdentifier) {
        return this.loginAttemptRepository.findByAccountIdentifier(accountIdentifier);
    }
}
