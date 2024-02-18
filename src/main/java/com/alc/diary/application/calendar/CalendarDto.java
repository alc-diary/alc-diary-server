package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.Calendar;

import java.util.List;

public record CalendarDto(

        long id,
        long ownerId,
        String title,
        String drinkDate,
        List<UserCalendarDto> userCalendars,
        List<PhotoDto> photos) {

    public static CalendarDto fromDomainModel(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkDate().toString(),
                List.of(),
                List.of()
        );
    }

    public static CalendarDto fromDomainModelDetail(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkDate().toString(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendarDto::fromDomainModelWithDrinkRecords)
                        .toList(),
                calendar.getPhotos().stream()
                        .map(PhotoDto::fromDomainModel)
                        .toList()
        );
    }
}
