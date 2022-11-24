package com.example.alcdiary.domain.model.user;

import lombok.Getter;

@Getter
public class UserDrinkingCapacityModel {

    private EUserAlcoholType alcoholType;
    private int capacity;

    private UserDrinkingCapacityModel() {
    }

    public static UserDrinkingCapacityModel of(EUserAlcoholType alcoholType, int capacity) {
        UserDrinkingCapacityModel userDrinkingCapacityModel = new UserDrinkingCapacityModel();
        userDrinkingCapacityModel.alcoholType = alcoholType;
        userDrinkingCapacityModel.capacity = capacity;
        return userDrinkingCapacityModel;
    }
}
