package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.DescriptionStyle;

public record GetMainResponse(
        String nickname,
        DescriptionStyle descriptionStyle,
        Long overAlcoholLimit
) {

    public static GetMainResponse of(User user, long overAlcoholLimit) {
        return new GetMainResponse(
                user.getDetail().getNickname(),
                user.getDetail().getDescriptionStyle(),
                calculateOverAlcohol(overAlcoholLimit, user.getDetail().getNonAlcoholGoal())
        );
    }

    private static Long calculateOverAlcohol(long overAlcoholLimit, long nonAlcoholGoal) {
        if (overAlcoholLimit <= nonAlcoholGoal) return null;
        return overAlcoholLimit - nonAlcoholGoal;
    }
}
