package com.alc.diary.application.user.dto.request;

public record DeactivateUserAppRequest(

        long targetUserId,
        String reason
) {
}
