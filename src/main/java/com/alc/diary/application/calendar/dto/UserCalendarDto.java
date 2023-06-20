package com.alc.diary.application.calendar.dto;

import com.alc.diary.domain.usercalendar.UserCalendar;
import com.alc.diary.domain.usercalendar.UserCalendarStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserCalendarDto(

        Long id,
        Long userId,
        List<UserCalendarDrinkDto> drinks,
        String content,
        String condition,
        List<UserCalendarImageDto> images,
        UserCalendarStatus status
) {

    public static List<UserCalendarDto> from(Set<UserCalendar> userCalendars) {
        return userCalendars.stream()
                .map(userCalendar -> new UserCalendarDto(
                        userCalendar.getId(),
                        userCalendar.getUser().getId(),
                        UserCalendarDrinkDto.from(userCalendar.getDrinks()),
                        userCalendar.getContent(),
                        userCalendar.getCondition(),
                        UserCalendarImageDto.from(userCalendar.getImages()),
                        userCalendar.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
