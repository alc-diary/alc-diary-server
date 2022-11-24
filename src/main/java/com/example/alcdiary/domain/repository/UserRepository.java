package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;

public interface UserRepository {

    UserModel save(UserModel userModel);

    UserModel findById(UserIdModel userId);
}
