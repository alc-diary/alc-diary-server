package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Column(name = "token", length = 50)
    @Id
    private String token;

    @Column(name = "expired_at", nullable = false, updatable = false)
    private Long expiredAt;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
}
