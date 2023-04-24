package com.alc.diary.application.user.dto.request;

public record UpdateAlcoholLimitAndGoalAppRequest(
    float newPersonalAlcoholLimit,
    int newNonAlcoholGoal
) {
}
