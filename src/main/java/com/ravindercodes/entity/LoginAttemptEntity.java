package com.ravindercodes.entity;

import com.ravindercodes.entity.config.DateEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_attempt")
public class LoginAttemptEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String accountIdentifier;

    @NotBlank
    @Size(max = 120)
    private String ipAddress;

    @NotNull
    private int attemptCount = 0;

    @NotNull
    private LocalDateTime lastAttemptTime;

    @NotNull
    private boolean isBlocked = false;

    public LoginAttemptEntity(String accountIdentifier, String ipAddress) {
        this.accountIdentifier = accountIdentifier;
        this.ipAddress = ipAddress;
        this.attemptCount = 0;
        this.lastAttemptTime = LocalDateTime.now();
        this.isBlocked = false;
    }

}
