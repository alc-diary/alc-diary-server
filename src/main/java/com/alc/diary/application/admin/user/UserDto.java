package com.alc.diary.application.admin.user;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.domain.user.enums.UserStatus;

public record UserDto(

        long id,
        UserDetailDto detail,
        SocialType socialType,
        String socialId,
        UserStatus status,
        String email,
        GenderType gender,
        AgeRangeType ageRange,
        String profileImage) {

    public static UserDto fromDomainModel(User user) {
        return new UserDto(
                user.getId(),
                UserDetailDto.fromDomainModel(user.getDetail()),
                user.getSocialType(),
                user.getSocialId(),
                user.getStatus(),
                user.getEmail(),
                user.getGender(),
                user.getAgeRange(),
                user.getProfileImage()
        );
    }
}
