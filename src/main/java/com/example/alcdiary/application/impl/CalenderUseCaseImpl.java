package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.domain.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CalenderUseCaseImpl implements CalenderUseCase {

    private final CalenderService calenderService;

    @Override
    public FindCalenderResult find(String userId, Long calenderId){
         return new FindCalenderResult().
                fromModel(calenderService.find(userId,calenderId));
    }

    @Override
    public void create(CreateCalenderCommand command) {
        calenderService.save(command);
    }
}
