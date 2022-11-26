package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.ReIssueAccessTokenUseCase;
import com.example.alcdiary.application.command.ReIssueAccessTokenCommand;
import com.example.alcdiary.application.result.ReIssueAccessTokenResult;
import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
class ReIssueAccessTokenUseCaseImpl implements ReIssueAccessTokenUseCase {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @Override
    public ReIssueAccessTokenResult execute(ReIssueAccessTokenCommand command) {
        RefreshTokenModel findRefreshTokenModel = refreshTokenService.getBy(command.getBearerToken());
        if (!findRefreshTokenModel.validToken()) {
            throw new AlcException(AuthError.EXPIRED_REFRESH_TOKEN);
        }
        String generatedAccessToken = jwtProvider.generateToken(findRefreshTokenModel.getUser().getId());
        refreshTokenService.extendValidityPeriod(findRefreshTokenModel);

        return ReIssueAccessTokenResult.of(generatedAccessToken, findRefreshTokenModel);
    }
}
