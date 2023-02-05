package com.example.alcdiary.domain.model.calender;

import java.util.List;

public record SearchCalenderModel(
        Long calenderId,
        String title,
        String drinkType,
        List<String> userProfileImageUrls,
        String userId,
        Long drinkCount,
        String drinkTime
) {
}
