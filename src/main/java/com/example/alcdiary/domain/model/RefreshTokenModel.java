package com.example.alcdiary.domain.model;

import com.example.alcdiary.domain.model.user.UserModel;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefreshTokenModel {

    private String token;
    private UserModel user;
    private LocalDateTime expiredAt;

    public boolean validToken() {
        return expiredAt
                .isAfter(LocalDateTime.now());
    }

    public void extendValidityPeriod() {
        this.expiredAt = LocalDateTime.now().plusMonths(1L);
    }

    private RefreshTokenModel() {
    }

    public static RefreshTokenModel of(
            String token,
            UserModel userModel,
            LocalDateTime expiredAt
    ) {
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        refreshTokenModel.token = token;
        refreshTokenModel.user = userModel;
        refreshTokenModel.expiredAt = expiredAt;
        return refreshTokenModel;
    }
}
