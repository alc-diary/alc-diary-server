package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.repository.RefreshTokenRepository;
import com.example.alcdiary.domain.service.RefreshTokenService;
import com.example.alcdiary.domain.util.uuid.UUIDProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UUIDProvider uuidProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenModel getBy(UserModel userModel) {
        String token = uuidProvider.createUUID();
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .token(token)
                .expiredAt(1000L)
                .userModel(userModel)
                .build();

        return refreshTokenRepository.save(refreshTokenModel);
    }
}
