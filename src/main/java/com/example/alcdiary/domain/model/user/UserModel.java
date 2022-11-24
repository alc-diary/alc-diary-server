package com.example.alcdiary.domain.model.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserModel {

    private UserIdModel id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private EUserTheme theme;
    private EUserGender gender;
    private EUserAgeRange ageRange;
    private UserDrinkingCapacityModel drinkingCapacityModel;
    private UserPromiseModel promise;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserModel() {
    }

    public static UserModel of(
            UserIdModel idModel,
            String email,
            String profileImageUrl,
            EUserGender gender,
            EUserAgeRange ageRange
    ) {
        UserModel userModel = new UserModel();
        userModel.id = idModel;
        if (email != null) {
            userModel.email = email;
        } else {
            userModel.email = "";
        }
        if (profileImageUrl != null) {
            userModel.profileImageUrl = profileImageUrl;
        } else {
            userModel.profileImageUrl = "";
        }
        userModel.profileImageUrl = profileImageUrl;
        userModel.theme = EUserTheme.UNKNOWN;
        userModel.gender = gender;
        userModel.ageRange = ageRange;
        userModel.drinkingCapacityModel = UserDrinkingCapacityModel.of(EUserAlcoholType.UNKNOWN, 0);
        userModel.promise = UserPromiseModel.from(0);

        return userModel;
    }

    public static UserModel of(
            UserIdModel idModel,
            String email,
            String nickname,
            String profileImageUrl,
            EUserTheme theme,
            EUserGender gender,
            EUserAgeRange ageRange,
            UserDrinkingCapacityModel drinkingCapacityModel,
            UserPromiseModel userPromiseModel,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        UserModel userModel = new UserModel();
        userModel.id = idModel;
        userModel.email = email;
        userModel.nickname = nickname;
        userModel.profileImageUrl = profileImageUrl;
        userModel.theme = theme;
        userModel.gender = gender;
        userModel.ageRange = ageRange;
        userModel.drinkingCapacityModel = drinkingCapacityModel;
        userModel.promise = userPromiseModel;
        userModel.createdAt = createdAt;
        userModel.updatedAt = updatedAt;
        return userModel;
    }
}
