package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.UserCalendar;

import java.util.List;

public record CreateCalendarResponse(

        long calendarId,
        List<Long> userCalendarIds
) {

    public static CreateCalendarResponse from(Calendar calendar) {
        return new CreateCalendarResponse(
                calendar.getId(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendar::getId)
                        .toList()
        );
    }
}
