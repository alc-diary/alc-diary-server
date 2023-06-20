package com.alc.diary.domain.friendship.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendshipError implements ErrorModel {

    FRIENDSHIP_NOT_FOUND("FS_E0000", "엔티티를 찾을 수 없습니다."),
    INVALID_REQUEST("FS_E0001", "잘못된 요청입니다."),
    ALREADY_SENT_REQUEST("FS_E0002", "이미 전송된 요청입니다."),
    NO_PERMISSION_TO_ACCEPT("FS_E0003", "요청 수락 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE("FS_E0004", "친구를 삭제할 권한이 없습니다."),
    FROM_USER_NULL("FS_E0005", "FromUser cannot be null."),
    TO_USER_NULL("FS_E0006", "To user cannot be null."),
    MESSAGE_LENGTH_EXCEEDED("FS_E0007", "Message length cannot exceed 100 characters."),
    USER_ALIAS_LENGTH_EXCEEDED("FS_E0008", "User alias length cannot exceed 30 characters."),
    ;

    private final String code;
    private final String message;
}
