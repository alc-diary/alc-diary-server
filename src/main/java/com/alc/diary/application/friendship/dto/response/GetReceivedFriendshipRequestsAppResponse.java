package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;

public record GetReceivedFriendshipRequestsAppResponse(

        long friendshipId,
        long fromUserId,
        String fromUserNickname,
        String fromUserProfileImageUrl,
        long toUserId,
        String toUserNickname,
        String message,
        FriendshipStatus status
) {

    public static GetReceivedFriendshipRequestsAppResponse from(Friendship friendship) {
        return new GetReceivedFriendshipRequestsAppResponse(
                friendship.getId(),
                friendship.getFromUser().getId(),
                friendship.getFromUser().getNickname(),
                friendship.getFromUser().getProfileImage(),
                friendship.getToUser().getId(),
                friendship.getToUser().getNickname(),
                friendship.getMessage(),
                friendship.getStatus()
        );
    }
}
