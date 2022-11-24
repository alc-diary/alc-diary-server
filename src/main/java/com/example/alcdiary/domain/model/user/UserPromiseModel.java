package com.example.alcdiary.domain.model.user;

import lombok.Getter;

@Getter
public class UserPromiseModel {

    private int resolutionDays;

    private UserPromiseModel() {
    }

    public static UserPromiseModel from(int resolutionDays) {
        UserPromiseModel userPromiseModel = new UserPromiseModel();
        userPromiseModel.resolutionDays = resolutionDays;
        return userPromiseModel;
    }
}
