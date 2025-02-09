package com.ravindercodes.repository;

import com.ravindercodes.entity.LoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Long> {

    LoginAttemptEntity findByIpAddress(String ipAddress);

    Optional<LoginAttemptEntity> findByAccountIdentifier(String accountIdentifier);

}
