package com.alc.diary.application.admin.user;

import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;

public record UserDetailDto(

        long id,
        String nickname,
        AlcoholType alcoholType,
        float personalAlcoholLimit,
        int nonAlcoholGoal,
        DescriptionStyle descriptionStyle) {

    public static UserDetailDto fromDomainModel(UserDetail userDetail) {
        return new UserDetailDto(
                userDetail.getId(),
                userDetail.getNickname(),
                userDetail.getAlcoholType(),
                userDetail.getPersonalAlcoholLimit(),
                userDetail.getNonAlcoholGoal(),
                userDetail.getDescriptionStyle()
        );
    }
}
