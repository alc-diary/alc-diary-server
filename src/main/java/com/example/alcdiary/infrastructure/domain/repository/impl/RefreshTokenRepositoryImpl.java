package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.infrastructure.entity.RefreshToken;
import com.example.alcdiary.infrastructure.jpa.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshTokenModel save(RefreshTokenModel refreshTokenModel) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenModel.getToken())
                .userId(refreshTokenModel.getUserModel().getId())
                .expiredAt(1000L)
                .build();
        RefreshToken savedRefreshToken = refreshTokenJpaRepository.save(refreshToken);

        return refreshTokenModel;
    }
}
