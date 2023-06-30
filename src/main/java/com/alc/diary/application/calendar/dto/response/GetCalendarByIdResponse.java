package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;

import java.util.List;

public record GetCalendarByIdResponse(

        long calendarId,
        long ownerId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        List<UserCalendarDto> userCalendars
) {

    public static GetCalendarByIdResponse from(Calendar calendar) {
        return new GetCalendarByIdResponse(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkStartTime().toString(),
                calendar.getDrinkEndTime().toString(),
                calendar.getUserCalendars().stream()
                        .map(userCalendar -> new UserCalendarDto(
                                userCalendar.getId(),
                                userCalendar.getUserId(),
                                userCalendar.getContent(),
                                userCalendar.getCondition(),
                                userCalendar.getTotalPrice(),
                                userCalendar.getTotalCalories(),
                                userCalendar.getDrinks().stream()
                                        .map(userCalendarDrink -> new UserCalendarDrinkDto(
                                                userCalendarDrink.getId(),
                                                userCalendarDrink.getDrinkUnitInfoId(),
                                                userCalendarDrink.getCalories()
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
