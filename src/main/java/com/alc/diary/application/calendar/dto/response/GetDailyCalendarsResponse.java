package com.alc.diary.application.calendar.dto.response;

import java.util.List;

public record GetDailyCalendarsResponse(

        long calendarId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        List<Long> currentUserDrinks,
        float currentUserDrinkTotal,
        List<UserDto> taggedUsers
) {

    public record UserDto(

            long id,
            String profileImageUrl
    ) {
    }
}
