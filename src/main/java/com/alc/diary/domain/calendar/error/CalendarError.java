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
    ;

    private final String code;
    private final String message;
}
