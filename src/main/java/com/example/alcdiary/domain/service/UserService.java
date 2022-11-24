package com.example.alcdiary.domain.service;

import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;

public interface UserService {

    UserModel getBy(UserIdModel userIdModel);

    UserModel save(UserModel userModel);
}
