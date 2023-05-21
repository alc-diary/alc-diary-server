package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.user.enums.DescriptionStyle;

public record GetMainResponse(
        String nickname,
        DescriptionStyle descriptionStyle,
        Long overAlcoholLimit
) {
    public static GetMainResponse create(String nickname, DescriptionStyle descriptionStyle, long overAlcoholLimit, long nonAlcoholGoal) {
        return new GetMainResponse(
                nickname, descriptionStyle, calculateOverAlcohol(overAlcoholLimit, nonAlcoholGoal)
        );
    }

    private static Long calculateOverAlcohol(long overAlcoholLimit, long nonAlcoholGoal) {
        if (overAlcoholLimit <= nonAlcoholGoal) return null;
        return overAlcoholLimit - nonAlcoholGoal;
    }
}
