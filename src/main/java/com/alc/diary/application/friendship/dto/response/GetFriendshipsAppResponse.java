package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;

import java.util.List;
import java.util.stream.Collectors;

public record GetFriendshipsAppResponse(

        long friendshipId,
        String nickname,
        String label,
        String profileImageUrl
) {

    public static List<GetFriendshipsAppResponse> of(List<Friendship> friendships, long userId) {
        return friendships.stream()
                .map(friendship ->
                        new GetFriendshipsAppResponse(
                                friendship.getId(),
                                friendship.getFriendNicknameByUserId(userId),
                                friendship.getFriendAliasByUserId(userId),
                                friendship.getFriendProfileImageUrlByUserId(userId)
                        )
                )
                .collect(Collectors.toList());
    }
}
