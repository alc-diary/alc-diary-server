package com.example.alcdiary.application;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.presentation.dto.response.SearchCalenderResponse;

import java.util.List;

public interface CalenderUseCase {

    FindCalenderResult find(String userId, Long calenderId);

    void create(CreateCalenderCommand command);

    void createSimple(CreateCalenderCommand command);
    void update(UpdateCalenderCommand command, Long calenderId);

    void delete(Long calenderId, String userId);

    List<SearchCalenderResponse> search(SearchCalenderCommand command);

}
