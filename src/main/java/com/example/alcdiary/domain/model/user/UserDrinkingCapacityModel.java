package com.example.alcdiary.domain.model.user;

import lombok.Getter;

@Getter
public class UserDrinkingCapacityModel {

    private EUserAlcoholType alcoholType;
    private int capacity;

    public UserDrinkingCapacityModel(EUserAlcoholType alcoholType, int capacity) {
        this.alcoholType = alcoholType;
        this.capacity = capacity;
    }
}
