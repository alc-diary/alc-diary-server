package com.alc.diary.application.friendship.dto.response;

public record SearchUserWithFriendStatusByNicknameAppResponse(

        long userId,
        String profileImageUrl,
        String nickname,
        String status
) {
}
