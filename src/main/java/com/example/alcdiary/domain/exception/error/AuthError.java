package com.example.alcdiary.domain.exception.error;

import com.example.alcdiary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements ErrorModel {

    INVALID_AUTHORIZATION_HEADER("Invalid authorization header"),
    ;

    private final String message;
    }
