package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.FindCalenderUseCase;
import com.example.alcdiary.application.result.FindCalenderResult;
import com.example.alcdiary.domain.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindCalenderUseCaseImpl implements FindCalenderUseCase {

    private final CalenderService calenderService;

    @Override
    public FindCalenderResult execute(Long calenderId) {
        return new FindCalenderResult().fromModel(calenderService.find(calenderId));
    }
}
