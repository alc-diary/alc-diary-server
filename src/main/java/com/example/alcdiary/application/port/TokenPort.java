package com.example.alcdiary.application.port;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.TokenModel;

public interface TokenPort {

    TokenModel getToken(UserModel userModel);
}
