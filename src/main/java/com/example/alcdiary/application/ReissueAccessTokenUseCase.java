package com.example.alcdiary.application;

import com.example.alcdiary.application.result.ReissueAccessTokenResult;
import com.example.alcdiary.domain.model.token.AccessTokenModel;

public interface ReissueAccessTokenUseCase {

    public ReissueAccessTokenResult execute(String bearerToken);
}
