package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.user.UserModel;

public interface UserService {

    UserModel getBy(UserModel userModel);

    UserModel save(UserModel userModel);

    UserModel saveDefault(UserModel defaultUserModel);
}
