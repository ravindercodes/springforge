package com.ravindercodes.dto.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordEmailModel {

    private String toEmail;
    private String subject;
    private String username;
    private String verificationToken;

}
