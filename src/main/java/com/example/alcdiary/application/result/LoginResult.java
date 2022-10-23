package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.token.AccessTokenModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginResult {

    private String accessToken;
    private String refreshToken;

    public static LoginResult from(AccessTokenModel accessTokenModel, RefreshTokenModel refreshTokenModel) {
        return LoginResult.builder()
                .accessToken(accessTokenModel.getToken())
                .refreshToken(refreshTokenModel.getToken())
                .build();
    }
}
