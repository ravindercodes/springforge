package com.ravindercodes.dto.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationTokenModel {

    private String toEmail;
    private String subject;
    private String username;
    private String verificationToken;

}
