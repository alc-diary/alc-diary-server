package com.alc.diary.domain.calendar.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCalendarError implements ErrorModel {

    USER_CALENDAR_NOT_FOUND("UC_E0000", "The specified UserCalendar entity could not be found."),
    USER_NULL("UC_E0001", "User cannot be null."),
    STATUS_NULL("UC_E0002", "Status cannot be null."),
    NO_PERMISSION("UC_E0003", "User do not have permission to this user calendar"),
    CONTENT_LENGTH_EXCEEDED("UC_E0004", "UserCalendar content length cannot exceed 1000 characters."),
    ;

    private final String code;
    private final String message;
}
