package com.alc.diary.application.user;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.domain.user.enums.UserStatus;

public record UserDto(

        long id,
        SocialType socialType,
        String socialId,
        UserStatus status,
        String email,
        GenderType gender,
        AgeRangeType ageRange,
        String profileImage,
        UserDetailDto detail) {

    public static UserDto fromDomainModel(User user) {
        return new UserDto(
                user.getId(),
                user.getSocialType(),
                user.getSocialId(),
                user.getStatus(),
                user.getEmail(),
                user.getGender(),
                user.getAgeRange(),
                user.getProfileImage(),
                null);
    }

    public static UserDto fromDomainModelDetail(User user) {
        return new UserDto(
                user.getId(),
                user.getSocialType(),
                user.getSocialId(),
                user.getStatus(),
                user.getEmail(),
                user.getGender(),
                user.getAgeRange(),
                user.getProfileImage(),
                UserDetailDto.fromDomainModel(user.getDetail()));
    }
}
