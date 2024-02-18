package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.UserCalendar;

import java.util.List;

public record UserCalendarDto(

        long id,
        long userId,
        String content,
        String drinkCondition,
        boolean drinkingRecorded,
        List<DrinkRecordDto> drinkRecords
) {

    public static UserCalendarDto fromDomainModel(UserCalendar userCalendar) {
        return new UserCalendarDto(
                userCalendar.getId(),
                userCalendar.getUserId(),
                userCalendar.getContent(),
                userCalendar.getDrinkCondition(),
                userCalendar.getDrinkingRecorded(),
                List.of()
        );
    }

    public static UserCalendarDto fromDomainModelWithDrinkRecords(UserCalendar userCalendar) {
        return new UserCalendarDto(
                userCalendar.getId(),
                userCalendar.getUserId(),
                userCalendar.getContent(),
                userCalendar.getDrinkCondition(),
                userCalendar.getDrinkingRecorded(),
                userCalendar.getDrinkRecords().stream()
                        .map(DrinkRecordDto::fromDomainModel)
                        .toList()
        );
    }
}
