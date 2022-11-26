package com.example.alcdiary.application;

import com.example.alcdiary.application.command.DeleteUsernameKeywordCommand;

public interface DeleteUsernameKeywordUseCase {

    void execute(DeleteUsernameKeywordCommand command);
}
