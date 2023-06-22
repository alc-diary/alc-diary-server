package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import com.alc.diary.domain.user.User;

public record SearchUserInfoAndFriendshipStatusByNicknameAppResponse(

        long userId,
        String profileImageUrl,
        String nickname,
        FriendshipStatus friendshipStatus
) {

    public static SearchUserInfoAndFriendshipStatusByNicknameAppResponse of(User user, FriendshipStatus friendshipStatus) {
        return new SearchUserInfoAndFriendshipStatusByNicknameAppResponse(
                user.getId(),
                user.getProfileImage(),
                user.getNickname(),
                friendshipStatus
        );
    }
}
