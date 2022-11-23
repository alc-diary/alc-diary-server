package com.example.alcdiary.domain.model.user;

public enum EUserServiceType {

    KAKAO("K"),
    GOOGLE("G"),
    ;

    private final String code;

    EUserServiceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
