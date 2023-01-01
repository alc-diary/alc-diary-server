package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.GetNicknameIsAvailableUseCase;
import com.example.alcdiary.application.command.GetNicknameIsAvailableCommand;
import com.example.alcdiary.application.result.GetNicknameIsAvailableResult;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetNicknameIsAvailableUseCaseImpl implements GetNicknameIsAvailableUseCase {

    private final UserService userService;

    @Override
    public GetNicknameIsAvailableResult execute(GetNicknameIsAvailableCommand command) {
        Optional<UserModel> optionalUserModel = userService.findByNickname(command.getContent());
        return new GetNicknameIsAvailableResult(
                !optionalUserModel.isPresent()
        );
    }
}
