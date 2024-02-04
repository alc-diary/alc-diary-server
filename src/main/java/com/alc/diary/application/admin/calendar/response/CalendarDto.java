package com.alc.diary.application.admin.calendar.response;

import com.alc.diary.domain.calendar.Calendar;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public record CalendarDto(

        long id,
        long ownerId,
        String title,
        float totalDrinkQuantity,
        ZonedDateTime drinkStartTime,
        ZonedDateTime drinkEndTime,
        List<UserCalendarDto> userCalendars,
        List<PhotoDto> photos,
        LocalDateTime deletedAt
) {

    public static CalendarDto fromDomainModel(Calendar calendar) {
        return new CalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getTotalDrinkQuantity(),
                calendar.getDrinkStartTime(),
                calendar.getDrinkEndTime(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendarDto::fromDomainModel)
                        .toList(),
                calendar.getPhotos().stream()
                        .map(PhotoDto::fromDomainModel)
                        .toList(),
                calendar.getDeletedAt()
        );
    }
}
