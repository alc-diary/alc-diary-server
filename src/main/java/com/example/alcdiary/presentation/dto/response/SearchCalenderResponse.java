package com.example.alcdiary.presentation.dto.response;

public record SearchCalenderResponse(
        Long calenderId,
        String title,
        String drinkType,
        Long drinkCount,
//        Map<String, String> friends,
        String drinkTime
){}