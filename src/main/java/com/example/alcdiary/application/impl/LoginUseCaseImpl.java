package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.LoginUseCase;
import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.application.port.AuthPort;
import com.example.alcdiary.application.result.LoginResult;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.model.AuthModel;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.AccessTokenModel;
import com.example.alcdiary.domain.model.token.RefreshTokenModel;
import com.example.alcdiary.domain.service.AccessTokenService;
import com.example.alcdiary.domain.service.RefreshTokenService;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthPort authPort;
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResult execute(LoginCommand loginCommand) {
        AuthModel authModel = authPort
                .service(loginCommand.getSocialType())
                .token(loginCommand.getToken())
                .authentication();
        UserModel userModel;
        try {
            userModel = userService.getBy(authModel);
        } catch (AlcException e) {
            UserModel userToSave = UserModel.builder()
                    .id(authModel.getId())
                    .email(authModel.getEmail())
                    .socialType(authModel.getSocialType())
                    .profileImageUrl(authModel.getProfileImageUrl())
                    .build();
            userModel = userService.save(userToSave);
        }

        AccessTokenModel accessTokenModel = accessTokenService.getBy(userModel);
        RefreshTokenModel refreshTokenModel = refreshTokenService.getBy(userModel);

        return LoginResult.from(accessTokenModel, refreshTokenModel);
    }
}
