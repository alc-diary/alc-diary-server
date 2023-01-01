package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.GetNicknameIsAvailableUseCase;
import com.example.alcdiary.application.command.GetNicknameIsAvailableCommand;
import com.example.alcdiary.application.result.GetNicknameIsAvailableResult;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetNicknameIsAvailableUseCaseImpl implements GetNicknameIsAvailableUseCase {

    private final UserService userService;

    @Override
    public GetNicknameIsAvailableResult execute(GetNicknameIsAvailableCommand command) {
        String trimNickname = command.getContent().trim();
        if (!StringUtils.hasText(trimNickname)) {
            throw new IllegalArgumentException("Invalid nickname");
        }
        Optional<UserModel> optionalUserModel = userService.findByNickname(trimNickname);
        return new GetNicknameIsAvailableResult(
                !optionalUserModel.isPresent()
        );
    }
}
