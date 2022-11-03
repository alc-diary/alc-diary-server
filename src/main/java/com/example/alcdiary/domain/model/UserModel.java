package com.example.alcdiary.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserModel {

    private String id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private OnBoardingStatus onBoardingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Gender {

        MAN, WOMAN,
    }

    public enum OnBoardingStatus {

        DEFAULT, DONE,
    }

}
