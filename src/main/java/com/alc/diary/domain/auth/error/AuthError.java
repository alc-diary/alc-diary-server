package com.alc.diary.domain.auth.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements ErrorModel {

    NO_AUTHORIZATION_HEADER("AU_E0000", "No authorization header."),
    INVALID_ACCESS_TOKEN("AU_E0001", "Invalid access token."),
    EXPIRED_ACCESS_TOKEN("AU_E0002", "Expired access token."),
    INVALID_REFRESH_TOKEN("AU_E0003", "Invalid refresh token."),
    EXPIRED_REFRESH_TOKEN("AU_E0004", "Expired refresh token."),
    INVALID_BEARER_TOKEN_FORMAT("AU_E0005", "Invalid bearer token format"),
    ;

    private final String code;
    private final String message;
}
