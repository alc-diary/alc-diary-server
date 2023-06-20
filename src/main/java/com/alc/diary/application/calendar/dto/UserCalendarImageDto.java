package com.alc.diary.application.calendar.dto;

import com.alc.diary.domain.usercalendar.UserCalendarImage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserCalendarImageDto(

        Long id,
        Long userCalendarId,
        String imageUrl
) {

    public static List<UserCalendarImageDto> from(Set<UserCalendarImage> userCalendarImages) {
        return userCalendarImages.stream()
                .map(userCalendarImage -> new UserCalendarImageDto(
                        userCalendarImage.getId(),
                        userCalendarImage.getUserCalendar().getId(),
                        userCalendarImage.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
