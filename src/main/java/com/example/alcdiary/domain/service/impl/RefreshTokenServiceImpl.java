package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenModel generate(UserModel userModel) {
        String refreshToken = UUID.randomUUID().toString();
        LocalDateTime tokenExpiredAt = LocalDateTime.now().plusMonths(1L);
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.of(
                refreshToken,
                userModel,
                tokenExpiredAt
        );
        return refreshTokenRepository.save(refreshTokenModel);
    }

    @Override
    public RefreshTokenModel getBy(String bearerToken) {
        String refreshToken = bearerToken.substring("Bearer ".length());
        return refreshTokenRepository.findByToken(refreshToken);
    }

    @Override
    public void extendValidityPeriod(RefreshTokenModel refreshTokenModel) {
        refreshTokenModel.extendValidityPeriod();
        refreshTokenRepository.save(refreshTokenModel);
    }
}
