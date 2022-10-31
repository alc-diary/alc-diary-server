package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.infrastructure.entity.RefreshToken;
import com.example.alcdiary.infrastructure.entity.user.User;
import com.example.alcdiary.infrastructure.jpa.RefreshTokenJpaRepository;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public RefreshTokenModel save(RefreshTokenModel refreshTokenModel) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenModel.getToken())
                .userId(refreshTokenModel.getUserModel().getId())
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .build();
        RefreshToken savedRefreshToken = refreshTokenJpaRepository.save(refreshToken);

        return refreshTokenModel;
    }

    @Override
    public RefreshTokenModel findByUserId(Long userId) {
        RefreshToken refreshToken = refreshTokenJpaRepository.findByUserId(userId);
        UserModel userModel = userJpaRepository.findById(userId)
                .map(User::toModel)
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER));
        return refreshToken.toModel(userModel);
    }
}
