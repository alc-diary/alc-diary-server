package com.alc.diary.application.admin.calendar.response;

import com.alc.diary.domain.calendar.Calendar;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminCalendarDto(

        long id,
        long ownerId,
        String title,
        float totalDrinkQuantity,
        ZonedDateTime drinkStartTime,
        ZonedDateTime drinkEndTime,
        LocalDate drinkDate,
        List<UserCalendarDto> userCalendars,
        List<PhotoDto> photos,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminCalendarDto fromDomainModel(Calendar calendar) {
        return new AdminCalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getTotalDrinkQuantity(),
                calendar.getDrinkStartTime(),
                calendar.getDrinkEndTime(),
                calendar.getDrinkDate(),
                null,
                null,
                calendar.getDeletedAt(),
                calendar.getCreatedAt(),
                calendar.getUpdatedAt()
        );
    }

    public static AdminCalendarDto fromDomainModelWithDetails(Calendar calendar) {
        return new AdminCalendarDto(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getTotalDrinkQuantity(),
                calendar.getDrinkStartTime(),
                calendar.getDrinkEndTime(),
                calendar.getDrinkDate(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendarDto::fromDomainModel)
                        .toList(),
                calendar.getPhotos().stream()
                        .map(PhotoDto::fromDomainModel)
                        .toList(),
                calendar.getDeletedAt(),
                calendar.getCreatedAt(),
                calendar.getUpdatedAt()
        );
    }
}
