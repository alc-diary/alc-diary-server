package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.AccessTokenModel;
import com.example.alcdiary.domain.service.AccessTokenService;
import com.example.alcdiary.domain.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JwtProvider jwtProvider;

    @Override
    public AccessTokenModel getBy(UserModel userModel) {
        String token = jwtProvider.createToken(userModel);
        return AccessTokenModel.builder()
                .token(token)
                .build();
    }
}
