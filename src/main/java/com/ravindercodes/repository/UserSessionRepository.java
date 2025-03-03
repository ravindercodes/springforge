package com.ravindercodes.repository;

import com.ravindercodes.entity.UserSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {

    Page<UserSessionEntity> findByUserIdAndActiveTrue(Long userId, Pageable pageable);

    List<UserSessionEntity> findByUserIdAndActiveTrue(Long userId);

    Page<UserSessionEntity> findByUserIdAndActiveFalse(Long userId, Pageable pageable);

    Optional<UserSessionEntity> findByDeviceId(String deviceId);

}
