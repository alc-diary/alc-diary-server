package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.LoginUseCase;
import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.application.port.CreateUserPort;
import com.example.alcdiary.application.port.LoadUserPort;
import com.example.alcdiary.application.result.LoginResult;
import com.example.alcdiary.domain.enums.SocialType;
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

    private final LoadUserPort loadUserPort;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResult execute(LoginCommand loginCommand) {
        UserModel userModel = loadUserPort.load(SocialType.KAKAO, loginCommand.getToken());
        AccessTokenModel accessTokenModel = accessTokenService.generate(userModel);
        RefreshTokenModel refreshTokenModel = refreshTokenService.generate(userModel);

        return LoginResult.from(
                accessTokenModel,
                refreshTokenModel
        );
    }
}
