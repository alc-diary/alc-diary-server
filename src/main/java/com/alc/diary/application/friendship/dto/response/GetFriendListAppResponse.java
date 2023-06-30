package com.alc.diary.application.friendship.dto.response;

public record GetFriendListAppResponse(

        long friendId,
        long friendshipId,
        long friendUserId,
        String friendNickname,
        String friendLabel,
        String friendProfileImageUrl
) {
}
