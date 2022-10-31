package com.example.alcdiary.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserModel {

    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private SocialType socialType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum SocialType {

        KAKAO,
        GOOGLE,
    }

    public enum Gender {

        MAN, WOMAN,
    }
}
