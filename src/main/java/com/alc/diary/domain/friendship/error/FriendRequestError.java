package com.alc.diary.domain.friendship.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendRequestError implements ErrorModel {

    SENDER_ID_NULL("FR_E0000", "Sender ID cannot be null."),
    RECEIVER_ID_NULL("FR_E0001", "Receiver ID cannot be null."),
    STATUS_NULL("FR_E0002", "Status cannot be null."),
    MESSAGE_LENGTH_EXCEEDED("FR_E0003", "Message length cannot exceed 100 characters."),
    INVALID_REQUEST("FR_E0004", "The request is invalid. Please check your input and try again."),
    NO_PERMISSION("FR_E0005", "You do not have permission to this request."),
    FRIEND_REQUEST_NOT_FOUND("FR_E0006", "The specified friend request entity could not be found."),
    FRIENDSHIP_LIMIT_EXCEEDED("FR_E0007", "Friendship limit exceeded."),
    CANNOT_ADD_SELF_AS_FRIEND("FR_E0008", "Cannot add self as friend."),
    ;

    private final String code;
    private final String message;
}
