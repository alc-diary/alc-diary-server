package com.alc.diary.application.calendar.dto.request;

import java.time.LocalDate;

public record UpdateCalendarRequestV2(

        String title,
        LocalDate drinkDate
) {
}
