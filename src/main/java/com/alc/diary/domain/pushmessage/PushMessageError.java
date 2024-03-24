package com.alc.diary.domain.pushmessage;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushMessageError implements ErrorModel {

    NOT_FOUND("PUSH_MESSAGE_E0000", "PushMessage entity not found"),
    ;

    private final String code;
    private final String message;
}
