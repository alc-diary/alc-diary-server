package com.alc.diary.domain.user.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGroupError implements ErrorModel {

    NOT_FOUND("USER_GROUP_E0000", "UserGroup entity not found"),
    ;

    private final String code;
    private final String message;
}
