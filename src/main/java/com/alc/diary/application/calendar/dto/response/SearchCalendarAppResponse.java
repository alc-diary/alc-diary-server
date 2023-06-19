package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;

import java.util.List;
import java.util.Set;

public record SearchCalendarAppResponse(

        List<CalendarDto> calendars
) {

    public static SearchCalendarAppResponse from(List<Calendar> calendars) {
        return null;
//        return new SearchCalendarAppResponse(
//                calendars.stream()
//                        .flatMap(calendar -> calendar.getUserCalendars().stream())
//                        .map(userCalendar -> new CalendarDto(userCalendar.getCalendar().getDrinkStartTime(), DrinkType.BEER))
//                        .collect(Collectors.toList()));
    }

    private record CalendarDto(

            long calendarId,
            String title,
            String drinkStartTime,
            String drinkEndTime,
            UserCalendarDto userCalendar
    ) {

        private record UserCalendarDto(

                long userCalendarId,
                long userId,
                String content,
                String condition,
                Set<String> images
        ) {
        }
    }
}
