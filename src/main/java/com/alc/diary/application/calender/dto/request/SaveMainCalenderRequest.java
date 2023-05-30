package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.model.DrinkModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record SaveMainCalenderRequest(
        @NotNull(message = "음주 기록은 필수입니다.")
        @NotBlank(message = "음주 기록은 필수입니다.")
        List<DrinkModel> drinkModels,
        LocalDateTime drinkStartDateTime
) {
}
