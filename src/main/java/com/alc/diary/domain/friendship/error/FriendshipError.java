package com.alc.diary.domain.friendship.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendshipError implements ErrorModel {

    FRIENDSHIP_NOT_FOUND("FS_E0000", "The specified friendship entity could not be found."),
    INVALID_REQUEST("FS_E0001", "The request is invalid. Please check your input and try again."),
    ALREADY_SENT_REQUEST("FS_E0002", "A friendship request has already been sent to this user."),
    NO_PERMISSION_TO_ACCEPT("FS_E0003", "You do not have permission to accept this friendship request."),
    NO_PERMISSION_TO_DELETE("FS_E0004", "You do not have permission to delete this friend."),
    FROM_USER_NULL("FS_E0005", "FromUser cannot be null."),
    TO_USER_NULL("FS_E0006", "ToUser cannot be null."),
    MESSAGE_LENGTH_EXCEEDED("FS_E0007", "Message length cannot exceed 100 characters."),
    USER_ALIAS_LENGTH_EXCEEDED("FS_E0008", "User alias length cannot exceed 30 characters."),
    NO_PERMISSION_TO_DECLINE("FS_E0009", "You do not have permission to decline this friendship."),
    NO_PERMISSION("FS_E0010", "You do not have permission to this friendship."),
    ;

    private final String code;
    private final String message;
}
