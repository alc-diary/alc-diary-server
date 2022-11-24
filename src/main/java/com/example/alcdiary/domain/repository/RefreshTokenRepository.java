package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.RefreshTokenModel;

public interface RefreshTokenRepository {

    RefreshTokenModel save(RefreshTokenModel refreshTokenModel);
    RefreshTokenModel findByToken(String refreshToken);
}
