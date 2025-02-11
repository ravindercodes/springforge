package com.ravindercodes.service.impl;

import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.dto.response.SuccessResponse;
import com.ravindercodes.dto.response.UserSessionResponse;
import com.ravindercodes.entity.UserSessionEntity;
import com.ravindercodes.exception.custom.ResourceNotFoundEx;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.repository.UserSessionRepository;
import com.ravindercodes.service.UserSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSessionImpl implements UserSessionService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final UserSessionRepository userSessionRepository;

    public UserSessionImpl(UserRepository userRepository, ModelMapper modelMapper, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public ResponseEntity<?> logoutFromDevice(String deviceId) {
        UserSessionEntity session = this.userSessionRepository.findByDeviceId(deviceId).orElseThrow(() -> new ResourceNotFoundEx(MessagesConstants.RECORD_NOT_FOUND, HttpStatus.BAD_REQUEST));
        session.setActive(false);
        userSessionRepository.save(session);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.USER_LOGOUT_FROM_DEVICE, null));
    }

    @Override
    public ResponseEntity<?> logoutFromAllDevice(long userId) {
        List<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveTrue(userId);
        for (UserSessionEntity session : listUserSession) {
            session.setActive(false);
        }
        userSessionRepository.saveAll(listUserSession);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.USER_LOGOUT_FROM_ALL_DEVICE, null));
    }

    @Override
    public ResponseEntity<?> getActiveSessions(long userId) {
        List<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveTrue(userId);
        List<UserSessionResponse> userSessionResponses = listUserSession.stream()
                .map(session -> modelMapper.map(session, UserSessionResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.FETCH_RECORD_SUCCESSFULLY, userSessionResponses));
    }

    @Override
    public ResponseEntity<?> getDisabledSessions(long userId) {
        List<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveFalse(userId);
        List<UserSessionResponse> userSessionResponses = listUserSession.stream()
                .map(session -> modelMapper.map(session, UserSessionResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.FETCH_RECORD_SUCCESSFULLY, userSessionResponses));
    }
}
