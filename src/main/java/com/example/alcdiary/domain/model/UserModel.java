package com.example.alcdiary.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModel {

    private String id;
    private String username;
    private Gender gender;
    private String profileImgUrl;

    public enum Gender {

        MAN, WOMAN,
    }
}
