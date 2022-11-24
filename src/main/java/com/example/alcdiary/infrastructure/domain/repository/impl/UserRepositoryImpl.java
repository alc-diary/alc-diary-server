package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.infrastructure.entity.User;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel save(UserModel userModel) {
        User user = User.from(userModel);
        User savedUser = userJpaRepository.save(user);
        return savedUser.convertToDomainModel();
    }

    @Override
    public UserModel findById(UserIdModel userId) {
        User user = userJpaRepository
                .findById(userId.parse())
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER));
        return user.convertToDomainModel();
    }
}
