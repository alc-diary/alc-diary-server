package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.port.TokenPort;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.TokenModel;
import com.example.alcdiary.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenPort tokenPort;

    @Override
    public TokenModel getBy(UserModel userModel) {
        return tokenPort.getToken(userModel);
    }
}
