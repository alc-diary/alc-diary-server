package com.alc.diary.application.auth.strategy.dto;

import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;

public record SocialLoginStrategyResponse(
    SocialType socialType,
    String socialUserId,
    String profileImage,
    String email,
    GenderType gender,
    AgeRangeType ageRange
) {
}
