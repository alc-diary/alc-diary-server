package com.example.alcdiary.application;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;

public interface CalenderUseCase {

    FindCalenderResult find(Long calenderId);

    void create(CreateCalenderCommand command);
}
