package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CalenderError;
import com.example.alcdiary.domain.model.CalenderModel;
import com.example.alcdiary.infrastructure.jpa.CalenderRepository;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.infrastructure.entity.Calender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CalenderServiceImpl implements CalenderService {

    private final CalenderRepository calenderRepository;

    @Override
    public CalenderModel find(Long calenderId) {
        Calender calender = calenderRepository.findById(calenderId)
                .orElseThrow(() -> new AlcException(CalenderError.NOT_FOUND_CALENDER));
        return CalenderModel.builder()
                .id(calender.getId())
                .title(calender.getTitle())
                .build();
    }

    @Override
    public void save(CreateCalenderCommand command) {
        calenderRepository.save(
                Calender.builder()
                        .title(command.getTitle())
                        .contents(command.getContents())
                        .drinkType(command.getDrinkType())
                        .hangOver(command.getHangOver())
                        .build()
        );
    }
}
