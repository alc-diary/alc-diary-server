package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record GetCalendarByIdResponse(

        long calendarId,
        long ownerId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        List<UserCalendarDto> userCalendars
) {

    public static GetCalendarByIdResponse of(Calendar calendar, long userId) {
        return new GetCalendarByIdResponse(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(calendar.getDrinkStartTime()),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(calendar.getDrinkEndTime()),
                calendar.getUserCalendars().stream()
                        .map(userCalendar -> new UserCalendarDto(
                                userCalendar.getId(),
                                userCalendar.getUserId(),
                                userCalendar.isOwner(userId),
                                userCalendar.getContent(),
                                userCalendar.getCondition(),
                                userCalendar.getTotalPrice(),
                                userCalendar.getTotalCalories(),
                                userCalendar.getDrinks().stream()
                                        .map(userCalendarDrink -> new UserCalendarDrinkDto(
                                                userCalendarDrink.getId(),
                                                userCalendarDrink.getDrinkUnitInfoId(),
                                                userCalendarDrink.getQuantity()
                                        ))
                                        .toList(),
                                userCalendar.getImages().stream()
                                        .map(userCalendarImage -> new UserCalendarImageDto(
                                                userCalendarImage.getId(),
                                                userCalendarImage.getImageUrl()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    public record UserCalendarDto(

            long id,
            long userId,
            boolean isOwner,
            String content,
            String condition,
            int totalPrice,
            int totalCalories,
            List<UserCalendarDrinkDto> drinks,
            List<UserCalendarImageDto> images
    ) {
    }

    public record UserCalendarDrinkDto(

            long id,
            long drinkUnitInfoId,
            float quantity
    ) {
    }

    public record UserCalendarImageDto(

            long id,
            String imageUrl
    ) {
    }
}
