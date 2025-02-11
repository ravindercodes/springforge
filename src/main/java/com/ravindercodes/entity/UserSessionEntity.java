package com.ravindercodes.entity;

import com.ravindercodes.entity.config.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_sessions")
public class UserSessionEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    @Lob
    private String refreshToken;
    private boolean active;

    public UserSessionEntity(UserEntity user, String deviceId, String deviceName, String ipAddress, String refreshToken, boolean active) {
        this.user = user;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.refreshToken = refreshToken;
        this.active = active;
    }
}
