package com.alc.diary.application.user.dto.response;

import com.alc.diary.domain.user.enums.DescriptionStyle;

public record GetUserInfoAppResponse(
    DescriptionStyle descriptionStyle,
    String nickname,
    String alcoholType,
    int drinkAmount,
    int nonAlcoholGoal
) {
}
