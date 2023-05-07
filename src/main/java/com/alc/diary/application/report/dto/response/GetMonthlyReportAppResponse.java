package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.enums.DrinkType;

import java.time.DayOfWeek;
import java.util.List;

public record GetMonthlyReportAppResponse(

        float numberOfDrinks,
        int daysOfDrinking,
        DrinkType mostDrunkAlcoholType,
        List<DayOfWeek> mostDayOfWeek
) {
}
