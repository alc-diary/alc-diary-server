package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.infrastructure.entity.user.User;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel save(UserModel userModel) {
        User user = User.from(userModel);
        User savedUser = userJpaRepository.save(user);

        return savedUser.toModel();
    }

    @Override
    public UserModel findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(User::toModel)
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER));
    }
}
