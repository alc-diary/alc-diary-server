package com.alc.diary.application.friendship.dto.response;

public record GetFriendListAppResponse(

        long friendId,
        String nickname,
        String label,
        String profileImageUrl
) {
}
