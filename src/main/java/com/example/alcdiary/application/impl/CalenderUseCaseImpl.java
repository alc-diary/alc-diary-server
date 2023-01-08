package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.domain.enums.DrinkType;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.domain.service.UserCalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CalenderUseCaseImpl implements CalenderUseCase {

    private final CalenderService calenderService;
    private final UserCalenderService userCalenderService;

    @Override
    public FindCalenderResult find(String userId, Long calenderId) {
        // TODO: user_calender에서 join 걸어서 찾기
        return new FindCalenderResult().
                fromModel(calenderService.find(userId, calenderId));
    }

    @Override
    public void create(CreateCalenderCommand command) {
        command.setDrinkReport(DrinkType.calculate(command.getDrinks()));
        calenderService.saveUserAndCalenderData(command);
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
}
