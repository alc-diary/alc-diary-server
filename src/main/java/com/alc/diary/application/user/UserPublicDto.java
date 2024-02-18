package com.alc.diary.application.user;

import com.alc.diary.domain.user.User;

public record UserPublicDto(

        long id,
        String nickname,
        String profileImage) {

    public static UserPublicDto fromDomainModel(User user) {
        return new UserPublicDto(
                user.getId(),
                user.getDetail().getNickname(),
                user.getProfileImage());
    }
}
