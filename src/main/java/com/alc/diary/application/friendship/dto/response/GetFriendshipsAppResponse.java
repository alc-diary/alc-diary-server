package com.alc.diary.application.friendship.dto.response;

import com.alc.diary.domain.friendship.Friendship;

import java.util.List;
import java.util.stream.Collectors;

public record GetFriendshipsAppResponse(

        List<FriendshipDto> friendshipDtos
) {

    public static GetFriendshipsAppResponse of(List<Friendship> friendships, long userId) {
        return new GetFriendshipsAppResponse(friendships.stream()
                .map(friendship -> new FriendshipDto(
                        friendship.getId(),
                        friendship.getOtherUserNicknameByUserId(userId),
                        friendship.getAliasByUserId(userId)
                ))
                .collect(Collectors.toList())
        );
    }

    private record FriendshipDto(

            long friendshipId,
            String nickname,
            String alias
    ) {
    }
}
