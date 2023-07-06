package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.user.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
