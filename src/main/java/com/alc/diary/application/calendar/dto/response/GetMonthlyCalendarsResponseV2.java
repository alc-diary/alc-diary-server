package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.application.calendar.CalendarDto;
import com.alc.diary.domain.calendar.Calendar;

import java.time.LocalDate;
import java.util.List;

public record GetMonthlyCalendarsResponseV2(

        String date,
        List<CalendarDto> calendars
) {

    public static GetMonthlyCalendarsResponseV2 of(LocalDate date, List<Calendar> calendars) {
        return new GetMonthlyCalendarsResponseV2(
                date.toString(),
                calendars.stream()
                        .map(CalendarDto::fromDomainModel)
                        .toList());
    }
}
