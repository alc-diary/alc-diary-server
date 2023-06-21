package com.alc.diary.application.user.dto.response;

import com.alc.diary.domain.user.User;

public record SearchUserAppResponse(

        long userId,
        String profileImage,
        String nickname
) {

    public static SearchUserAppResponse from(User user) {
        return new SearchUserAppResponse(user.getId(), user.getProfileImage(), user.getDetail().getNickname());
    }
}
