package com.alc.diary.domain.friendship.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendError implements ErrorModel {

    USER_ID_NULL("FS_E0000", "User ID cannot be null."),
    FRIEND_LABEL_EXCEEDED("FS_E0001", "Friend's label cannot exceed 30 characters."),
    NO_PERMISSION("FS_E0002", "User do not have permission to this friendship."),
    ;

    private final String code;
    private final String message;
}
