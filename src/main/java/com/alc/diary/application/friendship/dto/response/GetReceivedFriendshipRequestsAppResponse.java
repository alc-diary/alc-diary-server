package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;

public record GetReceivedFriendshipRequestsAppResponse(
        long requestId,
        long fromUserId,
        long toUserId,
        FriendshipStatus status,
        String message
) {

    public static GetReceivedFriendshipRequestsAppResponse from(Friendship friendship) {
        return new GetReceivedFriendshipRequestsAppResponse(
                friendship.getId(),
                friendship.getFromUser().getId(),
                friendship.getToUser().getId(),
                friendship.getStatus(),
                friendship.getMessage()
        );
    }
}
