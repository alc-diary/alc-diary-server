package com.example.alcdiary.application;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;

public interface CalenderUseCase {

    FindCalenderResult find(String userId, Long calenderId);

    void create(CreateCalenderCommand command);

    void createSimple(CreateCalenderCommand command);
    void update(UpdateCalenderCommand command, Long calenderId);

    void delete(Long calenderId, String userId);
}
