package com.alc.diary.application.user.dto.response;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.UserStatus;

public record GetUserInfoAppResponse(
    Long userId,
    DescriptionStyle descriptionStyle,
    AlcoholType alcoholType,
    String nickname,
    float personalAlcoholLimit,
    int nonAlcoholGoal,
    String profileImage,
    UserStatus status
) {

    public static GetUserInfoAppResponse from(User user) {
        return new GetUserInfoAppResponse(
                user.getId(),
                user.getDetail().getDescriptionStyle(),
                user.getDetail().getAlcoholType(),
                user.getDetail().getNickname(),
                user.getDetail().getPersonalAlcoholLimit(),
                user.getDetail().getNonAlcoholGoal(),
                user.getProfileImage(),
                user.getStatus()
        );
    }
}
