package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.AlcoholType;

public record UpdateAlcoholLimitAndGoalAppRequest(
    float newPersonalAlcoholLimit,
    int newNonAlcoholGoal,
    AlcoholType newAlcoholType
) {
}
