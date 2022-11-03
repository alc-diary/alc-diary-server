package com.example.alcdiary.domain.model;

import com.example.alcdiary.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthModel {

    private SocialType socialType;
    private Long id;
    private String email;
    private String profileImageUrl;
}
