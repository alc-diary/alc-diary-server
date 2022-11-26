package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.infrastructure.entity.RefreshToken;
import com.example.alcdiary.infrastructure.entity.User;
import com.example.alcdiary.infrastructure.jpa.RefreshTokenJpaRepository;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public RefreshTokenModel save(RefreshTokenModel refreshTokenModel) {
        refreshTokenJpaRepository.save(RefreshToken.from(refreshTokenModel));
        return refreshTokenModel;
    }

    @Override
    public RefreshTokenModel findByToken(String token) {
        RefreshToken refreshToken = refreshTokenJpaRepository
                .findById(token)
                .orElseThrow(() -> new AlcException(AuthError.INVALID_REFRESH_TOKEN));
        User user = userJpaRepository
                .findById(refreshToken.getUserId())
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER));
        return refreshToken.convertToDomainModel(user);
    }
}
