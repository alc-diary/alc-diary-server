package com.alc.diary.application.friendship.dto.response;

import java.util.List;
import java.util.stream.Collectors;

public record GetFriendshipsAppResponse(

        long friendshipId,
        String nickname,
        String label,
        String profileImageUrl
) {
}
