package com.example.alcdiary.domain.model.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenModel {

    private String token;
    private Long expiredAt;
}
