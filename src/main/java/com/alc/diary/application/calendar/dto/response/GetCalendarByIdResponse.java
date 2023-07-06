package com.alc.diary.application.calendar.dto.response;

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
