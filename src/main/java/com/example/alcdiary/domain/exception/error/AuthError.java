package com.example.alcdiary.domain.exception.error;

import com.example.alcdiary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements ErrorModel {

    INVALID_AUTHORIZATION_HEADER("Invalid authorization header"),
    EXPIRED_REFRESH_TOKEN("Expired refresh token"),
    INVALID_REFRESH_TOKEN("Invalid refresh token"),
    EXPIRED_ACCESS_TOKEN("Expired access token"),
    INVALID_ACCESS_TOKEN("Invalid access token"),
    INVALID_KAKAO_TOKEN("Invalid kakao token")
    ;

    private final String message;
    }
