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
    TITLE_NULL("C_E0003", "Title cannot be null."),
    DRINK_START_TIME_NULL("C_E0004", "Drink start time cannot be null."),
    DRINK_END_TIME_NULL("C_E0005", "Drink end time cannot be null."),
    USER_CALENDAR_NULL("C_E0006", "UserCalendar cannot be null."),
    USER_CALENDARS_NULL("C_E0007", "UserCalendars cannot be null."),
    START_TIME_AFTER_END_TIME("C_E0008", "Start time cannot be after end time."),
    END_TIME_IN_FUTURE("C_E0009", "End time cannot be int the future."),
    ;

    private final String code;
    private final String message;
}
