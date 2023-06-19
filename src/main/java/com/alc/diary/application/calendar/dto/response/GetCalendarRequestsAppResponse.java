package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.usercalendar.UserCalendar;
import io.swagger.annotations.ApiModel;

import java.util.List;

public record GetCalendarRequestsAppResponse(

        List<CalendarDto> calendarDtos
) {

    public static GetCalendarRequestsAppResponse from(List<Calendar> calendars) {
        return new GetCalendarRequestsAppResponse(calendars.stream()
                .map(calendar -> new CalendarDto(
                        calendar.getId(),
                        calendar.getOwner().getId(),
                        calendar.getOwner().getNickname(),
                        calendar.getUserCalendars().stream().findFirst().map(UserCalendar::getId).orElse(0L),
                        calendar.getTitle(),
                        calendar.getDrinkStartTime().toString(),
                        calendar.getDrinkEndTime().toString()
                ))
                .toList());
    }

    @ApiModel(value = "GetCalendarRequests_CalendarDto")
    private record CalendarDto(

            long calendarId,
            long ownerId,
            String ownerNickname,
            long userCalendarId,
            String title,
            String drinkStartTime,
            String drinkEndTime
    ) {
    }
}
