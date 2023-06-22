package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;

public record GetReceivedFriendshipRequestsAppResponse(

        long friendshipId,
        long requestUserId,
        String requestUserNickname,
        String requestUserProfileImageUrl,
        String message
) {

    public static GetReceivedFriendshipRequestsAppResponse from(Friendship friendship) {
        return new GetReceivedFriendshipRequestsAppResponse(
                friendship.getId(),
                friendship.getFromUser().getId(),
                friendship.getFromUser().getNickname(),
                friendship.getFromUser().getProfileImage(),
                friendship.getMessage()
        );
    }
}
