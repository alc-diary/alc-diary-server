package com.example.alcdiary.application;

import com.example.alcdiary.application.command.SaveUserInfoCommand;

public interface SaveUserInfoUseCase {

    void execute(String bearerToken, SaveUserInfoCommand command);
}
