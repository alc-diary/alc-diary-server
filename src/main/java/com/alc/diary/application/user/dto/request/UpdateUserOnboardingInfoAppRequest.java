package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;

public record UpdateUserOnboardingInfoAppRequest(
    DescriptionStyle descriptionStyle,
    String nickname,
    AlcoholType alcoholType,
    float personalAlcoholLimit,
    int nonAlcoholGoal
) {
}
