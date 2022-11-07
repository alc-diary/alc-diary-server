package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REFRESH_TOKEN", indexes = @Index(name = "idx_user_id", columnList = "user_id", unique = true))
@Entity
public class RefreshToken {

    @Column(name = "token", length = 50)
    @Id
    private String token;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    public RefreshTokenModel convertToDomainModel(UserModel userModel) {
        return RefreshTokenModel.builder()
                .token(this.token)
                .expiredAt(this.expiredAt)
                .userModel(userModel)
                .build();
    }

    public void updateExpiredAt() {
        expiredAt = LocalDateTime.now().plusMonths(1);
    }
}
