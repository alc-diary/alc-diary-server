package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.SaveUserInfoUseCase;
import com.example.alcdiary.application.command.SaveUserInfoCommand;
import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
class SaveUserInfoUseCaseImpl implements SaveUserInfoUseCase {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public void execute(String bearerToken, SaveUserInfoCommand command) {
        UserIdModel findUserIdModel = jwtProvider.getKey(bearerToken);
        UserModel findUserModel = userService.getBy(findUserIdModel);
        findUserModel.updateOnBoardingUserInfo(
                command.getTheme(),
                command.getNickname(),
                command.getAlcoholType(),
                command.getDrinkCapacity(),
                command.getDecideDays()
        );
        userService.save(findUserModel);
    }
}
