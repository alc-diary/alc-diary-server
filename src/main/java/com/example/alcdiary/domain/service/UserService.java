package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.AuthModel;
import com.example.alcdiary.domain.model.UserModel;

public interface UserService {

    UserModel save(UserModel userModel);

    UserModel getBy(AuthModel authModel);
}
