package com.example.alcdiary.infrastructure.adapter;

import com.example.alcdiary.application.port.TokenPort;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.AccessTokenModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.model.token.TokenModel;
import org.springframework.stereotype.Service;

@Service
public class TokenAdapter implements TokenPort {

    @Override
    public TokenModel getToken(UserModel userModel) {
        AccessTokenModel accessTokenModel = AccessTokenModel.builder()
                .token(userModel.getUsername() + " accessToken")
                .expiredAt(1000L)
                .build();
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .token(userModel.getUsername() + " refreshToken")
                .expiredAt(1000L)
                .build();

        return TokenModel.builder()
                .accessTokenModel(accessTokenModel)
                .refreshTokenModel(refreshTokenModel)
                .build();
    }
}
