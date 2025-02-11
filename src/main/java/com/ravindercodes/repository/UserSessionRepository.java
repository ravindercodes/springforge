package com.ravindercodes.repository;

import com.ravindercodes.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {

    List<UserSessionEntity> findByUserIdAndActiveTrue(Long userId);

    List<UserSessionEntity> findByUserIdAndActiveFalse(Long userId);

    Optional<UserSessionEntity> findByDeviceId(String deviceId);

}
