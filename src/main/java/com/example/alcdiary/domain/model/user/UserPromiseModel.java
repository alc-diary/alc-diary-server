package com.example.alcdiary.domain.model.user;

import lombok.Getter;

@Getter
public class UserPromiseModel {

    private int decideDays;

    private UserPromiseModel() {
    }

    public static UserPromiseModel from(int decideDays) {
        UserPromiseModel userPromiseModel = new UserPromiseModel();
        userPromiseModel.decideDays = decideDays;
        return userPromiseModel;
    }
}
