package com.example.alcdiary.presentation.dto.response;

import com.example.alcdiary.application.result.LoginResult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class KakaoLoginResponse {

    private String accessToken;
    private Long accessTokenExpiredAt;
    private String refreshToken;
    private Long refreshTokenExpiredAt;

    public static KakaoLoginResponse from(LoginResult loginResult) {
        return KakaoLoginResponse.builder()
                .accessToken(loginResult.getAccessToken())
                .accessTokenExpiredAt(loginResult.getAccessTokenExpiredAt())
                .refreshToken(loginResult.getRefreshToken())
                .refreshTokenExpiredAt(loginResult.getRefreshTokenExpiredAt())
                .build();
    }
}
