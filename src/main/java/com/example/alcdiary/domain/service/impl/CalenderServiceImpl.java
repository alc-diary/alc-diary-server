package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.domain.enums.DrinkType;
import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CalenderError;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.example.alcdiary.infrastructure.entity.UserCalender;
import com.example.alcdiary.infrastructure.jpa.CalenderRepository;
import com.example.alcdiary.infrastructure.jpa.UserCalenderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CalenderServiceImpl implements CalenderService {
    private final CalenderRepository calenderRepository;
    private final UserCalenderRepository userCalenderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DEFAULT_TITLE = "오늘의 음주 기록";

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
    public void saveUserAndCalenderData(CreateCalenderCommand command) {
        try {
            Calender calender = Calender.builder()
                    .userId(command.getUserId())
                    .title((command.getTitle() == null) ? DEFAULT_TITLE : command.getTitle())
                    .drinks(objectMapper.writeValueAsString(command.getDrinks()))
                    .hangOver(command.getHangOver())
                    .drinkStartTime(command.getDrinkStartTime())
                    .drinkEndTime((command.getDrinkEndTime()== null) ? Time.valueOf(LocalDateTime.now().toLocalTime()): command.getDrinkEndTime())
                    .imageUrl(command.getImageUrl())
                    .contents(command.getContents())
                    .drinkReport(DrinkType.calculate(command.getDrinks()))
                    .build();
            calenderRepository.save(calender);
            userCalenderRepository.save(UserCalender.builder()
                    .editRole(EditRole.EDITOR)
                    .userId(command.getUserId())
                    .calenderId(calender.getId())
                    .build());
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    @Override
    @Transactional
    public void update(UpdateCalenderCommand command, Long calenderId) {
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
        try {
            calenderRepository.deleteCalenderByUserIdAndId(userId, calenderId);
        } catch (Exception exception) {
            throw new AlcException(CalenderError.DELETE_ERROR_CALENDER);
        }
    }
}
