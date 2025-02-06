package com.ravindercodes.service.impl;

import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.dto.request.LoginRequest;
import com.ravindercodes.dto.request.SignupRequest;
import com.ravindercodes.dto.response.SuccessResponse;
import com.ravindercodes.dto.response.LoginResponse;
import com.ravindercodes.entity.RoleEntity;
import com.ravindercodes.entity.UserEntity;
import com.ravindercodes.exception.custom.EmailExitEx;
import com.ravindercodes.exception.custom.JwtGenerationEx;
import com.ravindercodes.exception.custom.ResourceNotFoundEx;
import com.ravindercodes.exception.custom.UsernameExitEx;
import com.ravindercodes.repository.RoleRepository;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.security.serviceimpl.UserDetailsImpl;
import com.ravindercodes.service.UserService;
import com.ravindercodes.util.JwtUtils;
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
    private final JwtUtils jwtUtils;

    @Autowired
    private final ModelMapper modelMapper;

    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = null;
        try {
            jwtToken = jwtUtils.generateToken((userDetails.getUsername()));
        } catch (Exception e) {
            log.error("JWT generation failed for user: {}", String.valueOf(e));
            throw new JwtGenerationEx(MessagesConstants.JWT_GENERATION_EX, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        userEntity.setPassword(encoder.encode(signUpRequest.getPassword()));
        userEntity.setRoleEntities(roleEntities);
        userRepository.save(userEntity);

        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.USER_REGISTER_SUCCESSFULLY, null));
    }

    @Override
    public ResponseEntity<?> validateToken(String token) {
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.TOKEN_VALIDATE_SUCCESSFULLY, this.jwtUtils.validateToken(token)));
    }
}
