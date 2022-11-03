package com.example.alcdiary.domain.repository;

import com.example.alcdiary.domain.model.UserModel;

import java.util.Optional;

public interface UserRepository {

    UserModel save(UserModel userModel);
    Optional<UserModel> findByIdAndSocialType(Long id, UserModel.SocialType socialType);
}
