package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CalenderError;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.example.alcdiary.infrastructure.jpa.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                        .userId(command.getUserId())
                        .title(command.getTitle())
                        .friends(mapToString(command.getFriends()))
                        .drinks(Arrays.stream(command.getDrinks()).map(DrinksModel::toString).collect(Collectors.joining()))
                        .hangOver(command.getHangOver())
                        .drinkStartTime(command.getDrinkStartTime())
                        .drinkEndTime(command.getDrinkEndTime())
                        .imageUrl(mapToString(command.getImageUrl()))
                        .contents(command.getContents())
                        .build()
        );
    }
    <T> String mapToString(T[] data) {
        return Arrays.toString(data);
    }
}
