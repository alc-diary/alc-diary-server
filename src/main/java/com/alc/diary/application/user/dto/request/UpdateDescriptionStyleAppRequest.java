package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.DescriptionStyle;

import javax.validation.constraints.NotNull;

public record UpdateDescriptionStyleAppRequest(

        @NotNull(message = "코알리 모드는 필수입니다.") DescriptionStyle newDescriptionStyle
) {
}
