package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.UserModel;

public interface UserRepository {

    UserModel save(UserModel userModel);

    UserModel findById(Long userId);
}
