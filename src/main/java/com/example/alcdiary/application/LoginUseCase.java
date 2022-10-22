package com.example.alcdiary.application;

import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.application.result.LoginResult;

public interface LoginUseCase {

    LoginResult execute(LoginCommand loginCommand);
}
