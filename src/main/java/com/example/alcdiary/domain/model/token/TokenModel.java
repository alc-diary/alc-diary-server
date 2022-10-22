package com.example.alcdiary.domain.model.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenModel {

    private AccessTokenModel accessTokenModel;
    private RefreshTokenModel refreshTokenModel;
}
