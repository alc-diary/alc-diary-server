package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;

import java.util.List;
import java.util.stream.Collectors;

public record GetPendingRequestsAppResponse(

        long friendshipId,
        long requestReceiverId,
        String requestReceiverNickname
) {

    public static List<GetPendingRequestsAppResponse> from(List<Friendship> friendships) {
        return friendships.stream()
                .map(friendship -> new GetPendingRequestsAppResponse(
                        friendship.getId(),
                        friendship.getToUser().getId(),
                        friendship.getToUser().getNickname()
                ))
                .collect(Collectors.toList());
    }
}
