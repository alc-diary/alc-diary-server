package com.alc.diary.domain.calendar.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalendarError implements ErrorModel {

    CALENDAR_NOT_FOUND("C_E0000", "The specified calendar entity could not be found."),
    NO_PERMISSION("C_E0001", "You do not have permission to access this calendar."),
    OWNER_NULL("C_E0002", "Owner cannot be null."),
    NULL_TITLE("C_E0003", "Title cannot be null."),
    NULL_DRINK_START_TIME("C_E0004", "Drink start time cannot be null."),
    NULL_DRINK_END_TIME("C_E0005", "Drink end time cannot be null."),
    NULL_USER_CALENDAR("C_E0006", "UserCalendar cannot be null."),
    NULL_USER_CALENDARS("C_E0007", "UserCalendars cannot be null."),
    START_TIME_AFTER_END_TIME("C_E0008", "Start time cannot be after end time."),
    END_TIME_IN_FUTURE("C_E0009", "End time cannot be int the future."),
    TITLE_LENGTH_EXCEEDED("C_E0010", "Calendar title length cannot exceed 100 characters."),
    DUPLICATE_USER_CALENDAR("C_E0011", "A UserCalendar with the same userId already exists."),
    DEACTIVATED_USER_INCLUDE("C_E0012", "The request include an user who has already withdrawn."),
    INVALID_DRINK_QUANTITY_INCREMENT("C_E0013", "The drink quantity should increase in increments of 0.5."),
    NO_PERMISSION_TO_UPDATE_TITLE("C_E0014", "You do not have permission to update title."),
    INVALID_REQUEST("C_E0015", "Invalid request"),
    IMAGE_LIMIT_EXCEEDED("C_E0016", "The number of images cannot exceed 20."),
    ;

    private final String code;
    private final String message;
}
