package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.model.user.UserModel;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class RefreshToken {

    @Column(name = "token")
    @Id
    private String token;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public RefreshToken() {
    }

    public static RefreshToken from(RefreshTokenModel refreshTokenModel) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.token = refreshTokenModel.getToken();
        refreshToken.userId = refreshTokenModel.getUser().getId().parse();
        refreshToken.expiredAt = refreshTokenModel.getExpiredAt();
        return refreshToken;
    }

    public RefreshTokenModel convertToDomainModel(User user) {
        return RefreshTokenModel.of(
                this.token,
                user.convertToDomainModel(),
                this.expiredAt
        );
    }
}
