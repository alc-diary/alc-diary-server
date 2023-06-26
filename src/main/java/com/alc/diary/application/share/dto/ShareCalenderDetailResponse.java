package com.alc.diary.application.share.dto;

import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.List;

public record ShareCalenderDetailResponse(
        long writerId,
        long calenderId,
        String title,
        String contents,
        LocalDateTime drinkStartDateTime,
        LocalDateTime drinkEndDateTime,
        List<DrinkModel> drinkModels,
        List<String> images,
        String drinkCondition,
        float totalDrinkCount
){

    public static ShareCalenderDetailResponse of(FindCalenderDetailResponse response, long writerId, long calenderId){
        return new ShareCalenderDetailResponse(
                writerId, calenderId, response.title(), response.contents(), response.drinkStartDateTime(),
                response.drinkEndDateTime(), response.drinkModels(), response.images(), response.drinkCondition(), response.totalDrinkCount()
        );
    }
}
