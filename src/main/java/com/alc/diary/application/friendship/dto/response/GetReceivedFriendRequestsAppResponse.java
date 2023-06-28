package com.alc.diary.application.friendship.dto.response;

public record GetReceivedFriendRequestsAppResponse(

        long friendshipId,
        long senderId,
        String senderNickname,
        String senderProfileImageUrl,
        String message
) {
}
