package com.example.alcdiary.application;

import com.example.alcdiary.application.command.GetNicknameIsAvailableCommand;
import com.example.alcdiary.application.result.GetNicknameIsAvailableResult;

public interface GetNicknameIsAvailableUseCase {

    GetNicknameIsAvailableResult execute(GetNicknameIsAvailableCommand command);
}
