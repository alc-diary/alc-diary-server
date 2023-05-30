package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.exception.InvalidRequestException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record SaveCalenderRequest(
        @NotNull(message = "제목은 필수입니다.")
        String title,
        String contents,
        @NotNull(message = "음주 시작시간은 필수입니다.")
        LocalDateTime drinkStartDateTime,
        @NotNull(message = "음주 끝나는 시간은 필수입니다.")
        LocalDateTime drinkEndDateTime,
        @NotNull(message = "마신 음주 기록은 필수입니다.")
        @NotEmpty(message = "음주 기록은 필수입니다.")
        List<DrinkModel> drinkModels,
        List<String> images,
        String drinkCondition

) {
    public SaveCalenderRequest {
        if (images.size() > 5) throw new InvalidRequestException();
    }
}
