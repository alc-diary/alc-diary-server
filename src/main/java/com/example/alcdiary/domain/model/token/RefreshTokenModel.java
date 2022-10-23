package com.example.alcdiary.domain.model.token;

import com.example.alcdiary.domain.model.UserModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenModel {

    private String token;
    private Long expiredAt;
    private UserModel userModel;
}
