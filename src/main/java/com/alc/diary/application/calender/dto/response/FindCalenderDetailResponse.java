package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.List;

public record FindCalenderDetailResponse(
        String title,
        String contents,
        LocalDateTime drinkStartDateTime,
        LocalDateTime drinkEndDateTime,
        List<DrinkModel> drinkModels,
        List<String> images,
        String drinkCondition,
        int totalDrinkCount
) {

    public static FindCalenderDetailResponse of(String title, String contents, LocalDateTime drinkStartDateTime, LocalDateTime drinkEndDateTime,
                                             List<DrinkModel> drinkModels, List<String> images, String drinkCondition) {
        return new FindCalenderDetailResponse(
                title, contents, drinkStartDateTime, drinkEndDateTime, drinkModels, images, drinkCondition, drinkModels.stream()
                .mapToInt(DrinkModel::getQuantity).sum()
        );
    }
}
