package com.ravindercodes.security.serviceimpl;

import com.ravindercodes.entity.UserEntity;
import com.ravindercodes.exception.custom.UserNotFoundEx;
import com.ravindercodes.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundEx("User not found " + usernameOrEmail, HttpStatus.UNAUTHORIZED));

        return UserDetailsImpl.build(userEntity);
    }
}
