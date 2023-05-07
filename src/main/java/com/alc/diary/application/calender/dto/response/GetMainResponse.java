package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.user.enums.DescriptionStyle;

public record GetMainResponse(
        String nickname,
        DescriptionStyle descriptionStyle,
        float personalAlcoholLimit
) {
    static String description;

    public GetMainResponse(String nickname, DescriptionStyle descriptionStyle, float personalAlcoholLimit) {
        this.nickname = nickname;
        description = getDescription(descriptionStyle);
        this.descriptionStyle = descriptionStyle;
        this.personalAlcoholLimit = personalAlcoholLimit;
    }

    private String getDescription(DescriptionStyle descriptionStyle) {
        return switch (descriptionStyle) {
            case MILD -> "test";
            case MARA -> "test....MARA";
        };
    }
}
