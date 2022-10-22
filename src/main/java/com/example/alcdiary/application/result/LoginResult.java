package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.token.TokenModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginResult {

    private String accessToken;
    private Long accessTokenExpiredAt;
    private String refreshToken;
    private Long refreshTokenExpiredAt;

    public static LoginResult from(TokenModel tokenModel) {
        return LoginResult.builder()
                .accessToken(tokenModel.getAccessTokenModel().getToken())
                .accessTokenExpiredAt(tokenModel.getAccessTokenModel().getExpiredAt())
                .refreshToken(tokenModel.getRefreshTokenModel().getToken())
                .refreshTokenExpiredAt(tokenModel.getRefreshTokenModel().getExpiredAt())
                .build();
    }
}
