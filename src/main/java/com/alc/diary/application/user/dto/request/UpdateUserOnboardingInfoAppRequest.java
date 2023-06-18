package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record UpdateUserOnboardingInfoAppRequest(
        @NotNull(message = "DescriptionStyle은 필수입니다.") DescriptionStyle descriptionStyle,
        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.")
        String nickname,
        @NotNull(message = "주종 필수입니다.") AlcoholType alcoholType,
        @NotNull(message = "주량은 필수입니다.") Float personalAlcoholLimit,
        @NotNull(message = "금주 목표는 필수입니다.") Integer nonAlcoholGoal
) {
}
