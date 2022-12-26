package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CalenderError;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.example.alcdiary.infrastructure.jpa.CalenderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class CalenderServiceImpl implements CalenderService {
    private final CalenderRepository calenderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CalenderModel find(String userId, Long calenderId) {
        try {
            Calender calender = calenderRepository.findByUserIdAndId(userId, calenderId)
                    .orElseThrow(() -> new AlcException(CalenderError.NOT_FOUND_CALENDER));
            return CalenderModel.builder()
                    .id(calender.getId())
                    .title(calender.getTitle())
                    .hangOver(calender.getHangOver())
                    .drinks(objectMapper.readValue(calender.getDrinks(), DrinksModel[].class))
                    .build();
        } catch (Throwable e) {
            throw new AlcException(CalenderError.NOT_FOUND_CALENDER);
        }
    }

    @Override
    public void save(CreateCalenderCommand command) {
        try {
            calenderRepository.save(
                    Calender.builder()
                            .userId(command.getUserId())
                            .title(command.getTitle())
                            .friends(mapToString(command.getFriends()))
                            .drinks(objectMapper.writeValueAsString(command.getDrinks()))
                            .hangOver(command.getHangOver())
                            .drinkStartTime(command.getDrinkStartTime())
                            .drinkEndTime(command.getDrinkEndTime())
                            .imageUrl(mapToString(command.getImageUrl()))
                            .contents(command.getContents())
                            .build()
            );
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    <T> String mapToString(T[] data) {
        return Arrays.toString(data);
    }
}
