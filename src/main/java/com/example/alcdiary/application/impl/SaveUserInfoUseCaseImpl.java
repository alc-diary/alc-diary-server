package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.SaveUserInfoUseCase;
import com.example.alcdiary.application.command.SaveUserInfoCommand;
import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class SaveUserInfoUseCaseImpl implements SaveUserInfoUseCase {

    private final UserService userService;

    @Override
    public void execute(String bearerToken, SaveUserInfoCommand command) {
    }
}
