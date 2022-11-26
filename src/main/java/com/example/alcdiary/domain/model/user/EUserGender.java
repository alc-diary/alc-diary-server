package com.example.alcdiary.domain.model.user;

public enum EUserGender {

    MALE,
    FEMALE,
    UNKNOWN;

    public static EUserGender from(String s) {
        if (s.equals("male")) {
            return MALE;
        }
        if (s.equals("female")) {
            return FEMALE;
        }
        return UNKNOWN;
    }
}
