package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.List;

public record SaveMainCalenderRequest(
        List<DrinkModel> drinkModels,
        LocalDateTime drinkStartDateTime
) {
}
