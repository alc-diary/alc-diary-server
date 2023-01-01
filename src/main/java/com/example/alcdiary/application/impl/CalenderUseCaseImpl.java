package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.domain.enums.DrinkType;
import com.example.alcdiary.domain.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CalenderUseCaseImpl implements CalenderUseCase {

    private final CalenderService calenderService;

    @Override
    public FindCalenderResult find(String userId, Long calenderId) {
        return new FindCalenderResult().
                fromModel(calenderService.find(userId, calenderId));
    }

    @Override
    public void create(CreateCalenderCommand command) {
        CreateCalenderCommand.builder().drinkReport(DrinkType.calculate(command.getDrinks()));
        calenderService.save(command);
    }

    @Override
    public void update(UpdateCalenderCommand command, Long calenderId) {
        calenderService.update(command, calenderId);
    }

    @Override
    public void delete(Long calenderId, String userId) {
        calenderService.delete(calenderId, userId);
    }
}
