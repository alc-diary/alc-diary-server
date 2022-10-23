package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.AuthModel;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public UserModel getBy(AuthModel authModel) {
        return userRepository.findByIdAndSocialType(authModel.getId(), authModel.getSocialType())
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER));
    }
}
