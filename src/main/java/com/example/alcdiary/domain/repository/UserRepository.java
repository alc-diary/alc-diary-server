package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;

import java.util.Optional;

public interface UserRepository {

    UserModel save(UserModel userModel);

    UserModel findById(UserIdModel userId);

    Optional<UserModel> findByNickname(String username);
}
