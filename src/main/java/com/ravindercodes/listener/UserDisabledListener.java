package com.ravindercodes.listener;

import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.dto.model.EmailVerificationTokenModel;
import com.ravindercodes.exception.custom.UserDisabledEx;
import com.ravindercodes.exception.custom.UserNotFoundEx;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.util.EmailUtility;
import com.ravindercodes.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDisabledListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private JwtUtility jwtUtility;

    @EventListener
    public void onUserDisabledEvent(AuthenticationFailureDisabledEvent event) {
        String usernameOrEmail = event.getAuthentication().getName();

        userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).ifPresentOrElse(user -> {
            if (!user.isEnabled()) {
                String verificationToken = jwtUtility.emailVerificationToken(user.getUsername());

                user.setVerificationToken(verificationToken);
                userRepository.save(user);

                emailUtility.sendEmailVerificationToken(
                        EmailVerificationTokenModel.builder()
                                .toEmail(user.getEmail())
                                .username(user.getUsername())
                                .subject(MessagesConstants.SUBJECT_VERIFICATION_EMAIL)
                                .verificationToken(verificationToken)
                                .build()
                );

                throw new UserDisabledEx(MessagesConstants.USER_DISABLED_VERIFICATION_EMAIL_SENT, HttpStatus.UNAUTHORIZED);
            }
        }, () -> {
            throw new UserNotFoundEx(MessagesConstants.USER_NOT_FOUND + usernameOrEmail, HttpStatus.UNAUTHORIZED);
        });
    }
}
