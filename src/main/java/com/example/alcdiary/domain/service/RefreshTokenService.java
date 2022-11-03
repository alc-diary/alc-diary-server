package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;

public interface RefreshTokenService {

    RefreshTokenModel generate(UserModel userModel);

    RefreshTokenModel getBy(String bearerToken);
}
