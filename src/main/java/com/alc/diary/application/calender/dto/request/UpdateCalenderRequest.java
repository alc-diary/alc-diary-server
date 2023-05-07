package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateCalenderRequest(
        String title,
        String contents,
        LocalDateTime drinkStartDateTime,
        LocalDateTime drinkEndDateTime,
        List<DrinkModel> drinkModels,
        List<String> images,
        String drinkCondition
) {
}
