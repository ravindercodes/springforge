package com.ravindercodes.service.impl;

import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import com.ravindercodes.dto.model.ResetPasswordEmailModel;
import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.ResetPasswordRequest;
import com.ravindercodes.dto.request.SignupRequest;
import com.ravindercodes.dto.response.LoginResponse;
import com.ravindercodes.dto.response.SuccessResponse;
import com.ravindercodes.entity.RoleEntity;
import com.ravindercodes.entity.UserEntity;
import com.ravindercodes.exception.custom.*;
import com.ravindercodes.repository.RoleRepository;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.security.serviceimpl.UserDetailsImpl;
import com.ravindercodes.service.LoginAttemptService;
import com.ravindercodes.service.UserService;
import com.ravindercodes.util.EmailUtility;
import com.ravindercodes.util.IpAddressUtility;
import com.ravindercodes.util.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final JwtUtility jwtUtility;

    @Autowired
    private final EmailUtility emailUtility;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final LoginAttemptService loginAttemptService;

    @Autowired
    private final HttpServletRequest request;

    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtility jwtUtility, EmailUtility emailUtility, ModelMapper modelMapper, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtility = jwtUtility;
        this.emailUtility = emailUtility;
        this.modelMapper = modelMapper;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        String ipAddress = IpAddressUtility.getClientIp(request);
        if (loginAttemptService.isIpBlocked(ipAddress)) {
            long ipUnlockTime = loginAttemptService.getIpUnlockTime(ipAddress);
            throw new IpAddressBlockedEx(String.format(MessagesConstants.IP_ADDRESS_BLOCKED, ipUnlockTime), HttpStatus.FORBIDDEN);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtility.accessToken((userDetails.getUsername()));
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), userDetails.getEmail(), roles, jwtToken);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.LOGIN_SUCCESSFULLY, loginResponse));
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameExitEx(MessagesConstants.USERNAME_ALREADY_EXIT, HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailExitEx(MessagesConstants.EMAIL_ALREADY_EXIT, HttpStatus.CONFLICT);
        }
        Set<Long> roleIds = signUpRequest.getRole();
        Set<RoleEntity> roleEntities = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(
                        () -> new ResourceNotFoundEx(MessagesConstants.ROLE_NOT_FOUND + roleId, HttpStatus.BAD_REQUEST)))
                .collect(Collectors.toSet());

        UserEntity userEntity = modelMapper.map(signUpRequest, UserEntity.class);
        String verificationToken = this.jwtUtility.emailVerificationToken(userEntity.getUsername());
        userEntity.setPassword(encoder.encode(signUpRequest.getPassword()));
        userEntity.setRoleEntities(roleEntities);
        userEntity.setVerificationToken(verificationToken);
        userRepository.save(userEntity);
        emailUtility.sendEmailVerificationToken(
                EmailVerificationTokenModel.builder()
                        .toEmail(userEntity.getEmail())
                        .username(userEntity.getUsername())
                        .subject(MessagesConstants.SUBJECT_VERFIFICATION_EMAIL)
                        .verificationToken(verificationToken)
                        .build()
        );
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.VERIFICATION_EMAIL_SENT, null));
    }

    @Override
    public ResponseEntity<?> validateToken(String token) {
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.TOKEN_VALIDATE_SUCCESSFULLY, this.jwtUtility.validateToken(token)));
    }

    @Override
    public ResponseEntity<?> emailVerification(String verificationToken) {
        Map<String, Object> tokenValidation = this.jwtUtility.validateToken(verificationToken);
        if (!(boolean) tokenValidation.get("valid")) {
            return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.INVALID_TOKEN, tokenValidation));
        }
        String username = this.jwtUtility.getUserNameFromJwtToken(verificationToken);
        UserEntity userEntity = this.userRepository.findByUsername(username);
        if (userEntity.getVerificationToken() == null && userEntity.getVerificationToken() != verificationToken) {
            throw new EmailVerificationFailedEx(MessagesConstants.EMAIL_VERIFICATION_FAILED, HttpStatus.BAD_REQUEST);
        }
        userEntity.setEnabled(true);
        this.userRepository.save(userEntity);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.EMAIL_VERIFIED_SUCCESSFULLY, null));
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UserNotFoundEx(MessagesConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        String verificationToken = this.jwtUtility.emailVerificationToken(userEntity.getUsername());
        userEntity.setVerificationToken(verificationToken);
        this.userRepository.save(userEntity);
        emailUtility.resetPassword(
                ResetPasswordEmailModel.builder()
                        .toEmail(userEntity.getEmail())
                        .username(userEntity.getUsername())
                        .subject(MessagesConstants.SUBJECT_RESET_PASSWORD)
                        .verificationToken(verificationToken)
                        .build()
        );
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.RESET_PASSWORD_EMAIL_SENT, null));
    }

    @Override
    public ResponseEntity<?> resetPassword(String verificationToken) {
        Map<String, Object> tokenValidation = this.jwtUtility.validateToken(verificationToken);
        if (!(boolean) tokenValidation.get("valid")) {
            return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.INVALID_TOKEN, tokenValidation));
        }
        String username = this.jwtUtility.getUserNameFromJwtToken(verificationToken);
        UserEntity userEntity = this.userRepository.findByUsername(username);
        if (userEntity.getVerificationToken() == null && userEntity.getVerificationToken() != verificationToken) {
            throw new ResetPasswordFailedEx(MessagesConstants.INVALID_RESET_TOKEN, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.TOKEN_VALIDATE_SUCCESSFULLY, tokenValidation));
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Map<String, Object> tokenValidation = this.jwtUtility.validateToken(resetPasswordRequest.getVerificationToken());
        if (!(boolean) tokenValidation.get("valid")) {
            return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.INVALID_TOKEN, tokenValidation));
        }
        String username = this.jwtUtility.getUserNameFromJwtToken(resetPasswordRequest.getVerificationToken());
        UserEntity userEntity = this.userRepository.findByUsername(username);
        if (userEntity.getVerificationToken() == null && userEntity.getVerificationToken() != resetPasswordRequest.getVerificationToken()) {
            throw new ResetPasswordFailedEx(MessagesConstants.INVALID_RESET_TOKEN, HttpStatus.BAD_REQUEST);
        }
        userEntity.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
        this.userRepository.save(userEntity);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.RESET_PASSWORD_SUCCESSFULLY, null));
    }
}
