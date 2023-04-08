package com.alc.diary.domain.user.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserError implements ErrorModel {

    USER_NOT_FOUND("US_E0000", "User not found."),
    INVALID_PARAMETER_INCLUDE("US_E0001", "Invalid parameter include"),
    DUPLICATE_NICKNAME_TOKEN("US_E0002", "Duplicate nickname token."),
    ;
    private final String code;
    private final String message;
}
