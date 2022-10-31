package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.token.RefreshTokenModel;

public interface RefreshTokenRepository {

    RefreshTokenModel save(RefreshTokenModel refreshTokenModel);

    RefreshTokenModel findByUserId(Long userId);
}
