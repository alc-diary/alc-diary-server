package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.TokenModel;

public interface UserService {

    UserModel getBy(TokenModel tokenModel);
}
