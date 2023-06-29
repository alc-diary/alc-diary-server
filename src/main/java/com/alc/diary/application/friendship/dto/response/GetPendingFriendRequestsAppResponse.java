package com.alc.diary.application.friendship.dto.response;

public record GetPendingFriendRequestsAppResponse(

        long friendshipId,
        long receiverId,
        String receiverNickname,
        String receiverProfileImageUrl
) {
}
