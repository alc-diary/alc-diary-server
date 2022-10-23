package com.example.alcdiary.presentation.dto.response;

import com.example.alcdiary.application.result.LoginResult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class KakaoLoginResponse {

    private String accessToken;
    private String refreshToken;

    public static KakaoLoginResponse from(LoginResult loginResult) {
        return KakaoLoginResponse.builder()
                .accessToken(loginResult.getAccessToken())
                .refreshToken(loginResult.getRefreshToken())
                .build();
    }
}
