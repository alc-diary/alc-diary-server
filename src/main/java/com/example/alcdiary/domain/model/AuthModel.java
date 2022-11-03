package com.example.alcdiary.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthModel {

    private UserModel.SocialType socialType;
    private Long id;
    private String email;
    private String profileImageUrl;
}
