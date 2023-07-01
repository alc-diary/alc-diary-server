package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
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

    public static GetDailyCalendarsResponse of(Calendar calendar, long userId, Map<Long, User> userById) {
        return calendar.getUserCalendarOfUser(userId).map(userCalendar ->
                new GetDailyCalendarsResponse(
                        calendar.getId(),
                        calendar.getTitle(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(calendar.getDrinkStartTime()),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(calendar.getDrinkEndTime()),
                        userCalendar.getAllDrinkUnitInfoIds(),
                        userCalendar.getTotalQuantity(),
                        calendar.getUserCalendarsExcludingUser(userId).stream()
                                .map(taggedUserCalendar -> {
                                    User taggedUser = userById.get(taggedUserCalendar.getUserId());
                                    return new GetDailyCalendarsResponse.UserDto(
                                            taggedUser.getId(),
                                            taggedUser.getProfileImage()
                                    );
                                })
                                .toList()
                ))
                .orElse(null);
    }

    public record UserDto(

            long id,
            String profileImageUrl
    ) {
    }
}
