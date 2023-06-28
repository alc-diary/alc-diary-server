package com.alc.diary.application.friendship.dto.response;

public record GetPendingFriendRequestsAppResponse(

        long friendshipId,
        long requestReceiverId,
        String requestReceiverNickname,
        String receiverProfileImageUrl
) {
}
