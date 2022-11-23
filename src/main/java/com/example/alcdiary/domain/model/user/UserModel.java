package com.example.alcdiary.domain.model.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserModel {

    private UserIdModel id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private EUserTheme theme;
    private EUserGender gender;
    private EUserAgeGroup ageGroup;
    private UserDrinkingCapacityModel drinkingCapacityModel;
    private UserPromise promise;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
