package com.example.alcdiary.domain.model.user;

import lombok.Getter;

@Getter
public class UserPromise {

    private int resolutionDays;

    public UserPromise(int resolutionDays) {
        this.resolutionDays = resolutionDays;
    }
}
