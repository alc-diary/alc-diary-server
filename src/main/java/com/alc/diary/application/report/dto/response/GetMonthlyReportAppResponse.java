package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.enums.DrinkType;

public record GetMonthlyReportAppResponse(

        float numberOfDrinks,
        int daysOfDrinking,
        DrinkType mostDrunkAlcoholType
) {
}
