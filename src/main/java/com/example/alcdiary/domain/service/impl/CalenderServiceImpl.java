package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
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
import org.springframework.transaction.annotation.Transactional;

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
                    .drinkStartTime(calender.getDrinkStartTime())
                    .drinkEndTime(calender.getDrinkEndTime())
                    .createdAt(calender.getCreatedAt())
                    .hangOver(calender.getHangOver())
                    .contents(calender.getContents())
                    .drinks(objectMapper.readValue(calender.getDrinks(), DrinksModel[].class))
                    .imageUrl(calender.getImageUrl().split(","))
                    .build();
        } catch (Throwable e) {
            throw new AlcException(CalenderError.NOT_FOUND_CALENDER);
        }
    }

    @Override
    public Calender[] search(SearchCalenderCommand command) {
//        Calender[] calenders = calenderRepository.search(CalenderSpec.searchWith(command.getMonth(), command.getDay()));
        return new Calender[0];
    }

    @Override
    @Transactional
    public void save(CreateCalenderCommand command) {
        // TODO: user_calender 도 저장해야함
        try {
            calenderRepository.save(
                    Calender.builder()
                            .userId(command.getUserId())
                            .title(command.getTitle())
                            .drinks(objectMapper.writeValueAsString(command.getDrinks()))
                            .hangOver(command.getHangOver())
                            .drinkStartTime(command.getDrinkStartTime())
                            .drinkEndTime(command.getDrinkEndTime())
                            .imageUrl(command.getImageUrl())
                            .contents(command.getContents())
                            .drinkReport(command.getDrinkReport())
                            .build()
            );
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    @Override
    @Transactional
    public void update(UpdateCalenderCommand command, Long calenderId) {
        // TODO: user_calender 권한 조회 시 userId와 일치하는 것만
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new AlcException(CalenderError.NOT_FOUND_CALENDER));
        try {
            calender.update(
                    command.getTitle(), objectMapper.writeValueAsString(command.getDrinks()),
                    command.getHangOver(), command.getDrinkStartTime(),
                    command.getDrinkEndTime(), command.getImageUrl(), command.getContents()
            );
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    @Override
    @Transactional
    public void delete(Long calenderId, String userId) {
        // TODO: user_calender 권한 조회 시 userId와 일치하는 것만
        try {
            calenderRepository.deleteCalenderById(calenderId, userId);
        } catch (Exception exception) {
            throw new AlcException(CalenderError.DELETE_ERROR_CALENDER);
        }
    }
}
