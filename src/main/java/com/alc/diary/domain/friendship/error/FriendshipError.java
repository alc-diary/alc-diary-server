package com.alc.diary.domain.friendship.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendshipError implements ErrorModel {

    INVALID_REQUEST("FS_E0000", "잘못된 요청입니다."),
    ALREADY_SENT_REQUEST("FS_E0001", "이미 전송된 요청입니다."),
    ;
    private final String code;
    private final String message;
}
