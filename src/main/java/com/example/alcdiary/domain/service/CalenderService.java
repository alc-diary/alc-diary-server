package com.example.alcdiary.domain.service;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.SearchCalenderModel;

import java.util.List;

public interface CalenderService {

    CalenderModel find(String userId, Long calenderId);

    List<SearchCalenderModel>  search(SearchCalenderCommand command);

    void save(CreateCalenderCommand command);

    void update(UpdateCalenderCommand command, Long calenderId);

    void delete(Long calenderId, String userId);
}
