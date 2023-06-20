package com.alc.diary.application.calendar.dto;

import com.alc.diary.domain.calendar.Calendar;

import java.util.List;

public record CalendarDto(

        Long id,
        Long ownerId,
        String title,
        List<UserCalendarDto> userCalendars,
        String drinkStartTime,
        String drinkEndTime
) {

    public static CalendarDto from(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwner().getId(),
                calendar.getTitle(),
                UserCalendarDto.from(calendar.getUserCalendars()),
                calendar.getDrinkStartTime().toString(),
                calendar.getDrinkEndTime().toString()
        );
    }
}
