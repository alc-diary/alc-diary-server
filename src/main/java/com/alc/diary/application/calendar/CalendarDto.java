package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.Calendar;

import java.time.LocalDate;
import java.util.List;

public record CalendarDto(

        long id,
        long ownerId,
        String title,
        String drinkDate,
        List<UserCalendarDto> userCalendars
        ) {

    public static CalendarDto fromDomainModel(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkDate().toString(),
                List.of()
        );
    }

    public static CalendarDto fromDomainModelWithUserCalendars(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkDate().toString(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendarDto::fromDomainModelWithDrinkRecords)
                        .toList()
        );
    }
}
