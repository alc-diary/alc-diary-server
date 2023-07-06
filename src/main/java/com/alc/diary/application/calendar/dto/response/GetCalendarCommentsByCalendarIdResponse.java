package com.alc.diary.application.calendar.dto.response;

public record GetCalendarCommentsByCalendarIdResponse(

        long id,
        String text,
        long calendarId,
        long userId,
        String nickname,
        String profileImageUrl,
        boolean isDeleted
) {
}
