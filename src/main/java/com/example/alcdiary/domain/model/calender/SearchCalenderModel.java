package com.example.alcdiary.domain.model.calender;

public record SearchCalenderModel(
        Long calenderId,
        String title,
        String drinkType,
        Long drinkCount,
        //        Map<String, String> friends,
        String drinkTime
) {
}
