package com.alc.diary.application.admin.pushmessage;

import com.alc.diary.domain.pushmessage.PushMessage;

import java.time.LocalDateTime;

public record AdminPushMessageDto(

        long id,
        String title,
        String body,
        String eventName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminPushMessageDto fromDomain(PushMessage domain) {
        return new AdminPushMessageDto(
                domain.getId(),
                domain.getTitle(),
                domain.getBody(),
                domain.getEventName(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }
}
