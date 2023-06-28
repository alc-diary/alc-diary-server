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
    ;

    private final String code;
    private final String message;
}
