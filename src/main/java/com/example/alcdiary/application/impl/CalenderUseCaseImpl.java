package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.domain.enums.DrinkType;
import com.example.alcdiary.domain.model.calender.SearchCalenderModel;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.domain.service.UserCalenderService;
import com.example.alcdiary.presentation.dto.response.SearchCalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CalenderUseCaseImpl implements CalenderUseCase {

    private final CalenderService calenderService;
    private final UserCalenderService userCalenderService;

    @Override
    public FindCalenderResult find(String userId, Long calenderId) {
        return new FindCalenderResult().
                fromModel(calenderService.find(userId, calenderId));
    }

    @Override
    public void create(CreateCalenderCommand command) {
        command.setDrinkReport(DrinkType.calculate(command.getDrinks()));
        calenderService.save(command);
    }

    @Override
    public void createSimple(CreateCalenderCommand command) {
        calenderService.save(command);
    }

    @Override
    public void update(UpdateCalenderCommand command, Long calenderId) {
        if (!userCalenderService.validateUserRole(command.getUserId(), calenderId)) return;
        calenderService.update(command, calenderId);
    }

    @Override
    public void delete(Long calenderId, String userId) {
        if (!userCalenderService.validateUserRole(userId, calenderId)) return;
        calenderService.delete(calenderId, userId);
    }

    @Override
    public List<SearchCalenderResponse> search(SearchCalenderCommand command) {
        List<SearchCalenderModel> calenders = calenderService.search(command);
        return calenders.stream().map(calender -> new SearchCalenderResponse(
                calender.calenderId(),
                calender.title(),
                calender.drinkType(),
                calender.drinkCount(),
                calender.drinkTime()
        )).toList();
    }
}
