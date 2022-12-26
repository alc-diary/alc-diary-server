package com.example.alcdiary.domain.service;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.domain.model.calender.CalenderModel;

public interface CalenderService {

    CalenderModel find(Long calenderId);

    void save(CreateCalenderCommand command);
}
