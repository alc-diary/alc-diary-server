package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserModel saveDefault(UserModel defaultUserModel) {
        return userRepository.saveDefault(defaultUserModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public UserModel getBy(UserModel userModel) {
        return userRepository.findById(userModel.getId());
    }
}
