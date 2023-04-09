package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.exception.InvalidRequestException;

import java.time.LocalDateTime;
import java.util.List;

public record SaveCalenderRequest(
        String title,
        String contents,
        LocalDateTime drinkStartDateTime,
        LocalDateTime drinkEndDateTime,
        List<DrinkModel> drinkModels,
        List<String> images,
        String drinkCondition

) {

    public SaveCalenderRequest {
        if (images.size() > 5) throw new InvalidRequestException();
    }
}
