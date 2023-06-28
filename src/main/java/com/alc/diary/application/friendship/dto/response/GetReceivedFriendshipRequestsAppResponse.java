package com.alc.diary.application.friendship.dto.response;

public record GetReceivedFriendshipRequestsAppResponse(

        long friendshipId,
        long requestUserId,
        String requestUserNickname,
        String requestUserProfileImageUrl,
        String message
) {
}
