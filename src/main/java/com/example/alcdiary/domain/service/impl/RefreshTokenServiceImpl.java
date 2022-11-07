package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.domain.service.RefreshTokenService;
import com.example.alcdiary.domain.util.uuid.UUIDProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UUIDProvider uuidProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenModel generate(UserModel userModel) {
        String token = uuidProvider.createUUID();
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .token(token)
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .userModel(userModel)
                .build();

        return refreshTokenRepository.save(refreshTokenModel);
    }

    @Override
    public RefreshTokenModel getBy(String bearerToken) {
        String refreshToken = getRefreshTokenByBearerToken(bearerToken);
        RefreshTokenModel refreshTokenModel = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenModel.isExpired()) {
            refreshTokenRepository.delete(refreshTokenModel);
            throw new AlcException(AuthError.EXPIRED_REFRESH_TOKEN);
        }
        return refreshTokenModel;
    }

    private static String getRefreshTokenByBearerToken(String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new AlcException(AuthError.INVALID_AUTHORIZATION_HEADER);
        }
        return bearerToken.substring("Bearer ".length());
    }
}
