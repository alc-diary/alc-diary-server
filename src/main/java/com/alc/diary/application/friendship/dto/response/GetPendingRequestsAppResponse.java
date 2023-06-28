package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.FriendRequest;
import com.alc.diary.domain.friendship.Friendship;

import java.util.List;
import java.util.stream.Collectors;

public record GetPendingRequestsAppResponse(

        long friendshipId,
        long requestReceiverId,
        String requestReceiverNickname,
        String receiverProfileImageUrl
) {
}
