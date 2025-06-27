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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserSessionImpl implements UserSessionService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserSessionRepository userSessionRepository;

    public UserSessionImpl(UserRepository userRepository, ModelMapper modelMapper, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public ResponseEntity<?> logoutFromDevice(final String deviceId) {
        UserSessionEntity session = this.userSessionRepository.findByDeviceId(deviceId).orElseThrow(() -> new ResourceNotFoundEx(MessagesConstants.RECORD_NOT_FOUND, HttpStatus.BAD_REQUEST));
        session.setActive(false);
        userSessionRepository.save(session);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.USER_LOGOUT_FROM_DEVICE, null));
    }

    @Override
    public ResponseEntity<?> logoutFromAllDevice(final long userId) {
        List<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveTrue(userId);
        for (UserSessionEntity session : listUserSession) {
            session.setActive(false);
        }
        userSessionRepository.saveAll(listUserSession);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.USER_LOGOUT_FROM_ALL_DEVICE, null));
    }

    @Override
    public ResponseEntity<?> getActiveSessions(final long userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveTrue(userId, pageable);
        List<UserSessionResponse> userSessionResponses = listUserSession.map(session -> modelMapper.map(session, UserSessionResponse.class)).getContent();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("totalPage", listUserSession.getTotalPages());
        metadata.put("page", listUserSession.getNumber());
        metadata.put("size", listUserSession.getSize());
        metadata.put("total", listUserSession.getTotalElements());
        metadata.put("sortBy", sortBy);
        metadata.put("sortDir", sortDir);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.FETCH_RECORD_SUCCESSFULLY, userSessionResponses, metadata));
    }

    @Override
    public ResponseEntity<?> getDisabledSessions(final long userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserSessionEntity> listUserSession = userSessionRepository.findByUserIdAndActiveFalse(userId, pageable);
        List<UserSessionResponse> userSessionResponses = listUserSession.map(session -> modelMapper.map(session, UserSessionResponse.class)).getContent();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("totalPage", listUserSession.getTotalPages());
        metadata.put("page", listUserSession.getNumber());
        metadata.put("size", listUserSession.getSize());
        metadata.put("total", listUserSession.getTotalElements());
        metadata.put("sortBy", sortBy);
        metadata.put("sortDir", sortDir);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.FETCH_RECORD_SUCCESSFULLY, userSessionResponses, metadata));
    }
}
