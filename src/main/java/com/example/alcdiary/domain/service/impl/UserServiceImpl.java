package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public UserModel getBy(UserIdModel userIdModel) {
        return userRepository.findById(userIdModel);
    }
}
