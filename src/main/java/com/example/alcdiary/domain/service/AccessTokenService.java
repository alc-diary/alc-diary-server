package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.AccessTokenModel;

public interface AccessTokenService {

    AccessTokenModel generate(UserModel userModel);
}
