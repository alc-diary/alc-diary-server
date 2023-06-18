package com.alc.diary.application.friendship.dto.request;

public record RequestFriendshipAppRequest(

        String targetNickname,
        String alias,
        String message
) {
}
