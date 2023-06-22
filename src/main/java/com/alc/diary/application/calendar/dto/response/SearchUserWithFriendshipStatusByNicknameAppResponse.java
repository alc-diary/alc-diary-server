package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import com.alc.diary.domain.user.User;

public record SearchUserWithFriendshipStatusByNicknameAppResponse(

        long userId,
        String profileImageUrl,
        String nickname,
        FriendshipStatus friendshipStatus
) {

    public static SearchUserWithFriendshipStatusByNicknameAppResponse of(User user, FriendshipStatus friendshipStatus) {
        return new SearchUserWithFriendshipStatusByNicknameAppResponse(
                user.getId(),
                user.getProfileImage(),
                user.getNickname(),
                friendshipStatus
        );
    }
}
