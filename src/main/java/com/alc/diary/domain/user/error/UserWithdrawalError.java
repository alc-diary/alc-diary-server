package com.alc.diary.domain.user.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserWithdrawalError implements ErrorModel {

    USER_WITHDRAWAL_NOT_FOUND("UW_E0000", "UserWithdrawal entity not found."),
    ;

    private final String code;
    private final String message;
}
