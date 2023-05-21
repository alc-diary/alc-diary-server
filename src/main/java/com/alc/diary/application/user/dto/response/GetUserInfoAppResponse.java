package com.alc.diary.application.user.dto.response;

import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.UserStatus;

public record GetUserInfoAppResponse(
    Long userId,
    DescriptionStyle descriptionStyle,
    UserStatus status,
    AlcoholType alcoholType,
    String nickname,
    float personalAlcoholLimit,
    int nonAlcoholGoal,
    String profileImage
) {
}
