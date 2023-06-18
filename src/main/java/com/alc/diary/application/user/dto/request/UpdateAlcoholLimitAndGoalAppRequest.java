package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.AlcoholType;

import javax.validation.constraints.NotNull;

public record UpdateAlcoholLimitAndGoalAppRequest(
    @NotNull(message = "주량은 필수입니다.") float newPersonalAlcoholLimit,
    @NotNull(message = "금주 목표는 필수입니다.") int newNonAlcoholGoal,
    @NotNull(message = "주종은 필수입니다.") AlcoholType newAlcoholType
) {
}
