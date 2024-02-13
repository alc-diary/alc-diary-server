package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.UserCalendar;

import java.util.List;

public record CreateCalendarResponseV2(

        long calendarId,
        List<Long> userCalendarIds
) {

    public static CreateCalendarResponseV2 from(Calendar calendar) {
        return new CreateCalendarResponseV2(
                calendar.getId(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendar::getId)
                        .toList()
        );
    }
}
