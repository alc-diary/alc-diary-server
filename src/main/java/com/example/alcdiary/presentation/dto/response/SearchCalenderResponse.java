package com.example.alcdiary.presentation.dto.response;

import java.util.List;

public record SearchCalenderResponse(
        Long calenderId,
        String title,
        String drinkType,

        List<String> userProfileImageUrls,
        String userId,
        Long drinkCount,
        String drinkTime
) {
}