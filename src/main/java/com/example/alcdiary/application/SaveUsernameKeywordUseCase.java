package com.example.alcdiary.application;

import com.example.alcdiary.application.command.SaveUsernameKeywordCommand;

public interface SaveUsernameKeywordUseCase {

    void execute(SaveUsernameKeywordCommand command);
}
