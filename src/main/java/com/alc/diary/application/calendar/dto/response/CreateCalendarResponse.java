package com.alc.diary.application.calendar.dto.response;

import java.util.List;

public record CreateCalendarResponse(

        long calendarId,
        List<Long> userCalendarIds
) {
}
