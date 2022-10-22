package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.LoginUseCase;
import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.application.port.AuthPort;
import com.example.alcdiary.application.result.LoginResult;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.model.token.TokenModel;
import com.example.alcdiary.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthPort authPort;
    private final TokenService tokenService;

    @Override
    public LoginResult execute(LoginCommand loginCommand) {
        UserModel userModel = authPort
                .service(loginCommand.getService())
                .token(loginCommand.getToken())
                .authentication();

        TokenModel tokenModel = tokenService.getBy(userModel);

        return LoginResult.from(tokenModel);
    }


}
