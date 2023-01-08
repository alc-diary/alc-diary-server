package com.example.alcdiary.domain.service;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.infrastructure.entity.Calender;

public interface CalenderService {

    CalenderModel find(String userId, Long calenderId);

    Calender[] search(SearchCalenderCommand command);

    void saveUserAndCalenderData(CreateCalenderCommand command);

    void update(UpdateCalenderCommand command, Long calenderId);

    void delete(Long calenderId, String userId);
}
