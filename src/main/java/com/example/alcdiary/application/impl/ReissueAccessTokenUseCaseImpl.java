package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.ReissueAccessTokenUseCase;
import com.example.alcdiary.application.result.ReissueAccessTokenResult;
import com.example.alcdiary.domain.model.token.AccessTokenModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.service.AccessTokenService;
import com.example.alcdiary.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReissueAccessTokenUseCaseImpl implements ReissueAccessTokenUseCase {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    @Override
    public ReissueAccessTokenResult execute(String bearerToken) {
        RefreshTokenModel refreshTokenModel = refreshTokenService.getBy(bearerToken);
        AccessTokenModel accessTokenModel = accessTokenService.generate(refreshTokenModel.getUserModel());
        return ReissueAccessTokenResult.from(accessTokenModel);
    }
}
