package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.usercalendar.UserCalendarImage;

import java.util.List;
import java.util.stream.Collectors;

public record FindCalendarAppResponse(

        long calendarId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        List<UserCalendarDto> userCalendars,
        boolean isOwner
) {

    public static FindCalendarAppResponse of(User user, Calendar calendar) {
        return new FindCalendarAppResponse(
                calendar.getId(),
                calendar.getTitle(),
                calendar.getDrinkStartTime().toString(),
                calendar.getDrinkEndTime().toString(),
                calendar.getUserCalendars().stream()
                        .map(userCalendar -> new UserCalendarDto(
                                        userCalendar.getId(),
                                        userCalendar.getUser().getId(),
                                        userCalendar.getCalendar().getId(),
                                        userCalendar.getContent(),
                                        userCalendar.getCondition(),
                                        userCalendar.getImages().stream()
                                                .map(UserCalendarImage::getImageUrl)
                                                .collect(Collectors.toList())
                                )
                        )
                        .collect(Collectors.toList()),
                calendar.getOwner().equals(user)
        );
    }

    private record UserCalendarDto(

            long userCalendarId,
            long userId,
            long calendarId,
            String content,
            String condition,
            List<String> imageUrls
    ) {
    }
}
