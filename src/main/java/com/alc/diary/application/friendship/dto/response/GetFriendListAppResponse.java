package com.alc.diary.application.friendship.dto.response;

public record GetFriendListAppResponse(

        long friendId,
        String friendNickname,
        String friendLabel,
        String friendProfileImageUrl
) {
}
