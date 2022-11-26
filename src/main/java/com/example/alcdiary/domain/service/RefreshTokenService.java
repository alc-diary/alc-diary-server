package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.model.user.UserModel;

public interface RefreshTokenService {

    RefreshTokenModel generate(UserModel userModel);

    RefreshTokenModel getBy(String bearerToken);

    void extendValidityPeriod(RefreshTokenModel refreshTokenModel);
}
