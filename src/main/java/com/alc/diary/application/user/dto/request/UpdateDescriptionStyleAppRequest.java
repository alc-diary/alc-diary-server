package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.DescriptionStyle;

public record UpdateDescriptionStyleAppRequest(
        DescriptionStyle newDescriptionStyle
) {
}
