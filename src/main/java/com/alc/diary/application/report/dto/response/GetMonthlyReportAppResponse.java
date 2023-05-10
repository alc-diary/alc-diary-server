package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.BeverageSummary;
import com.alc.diary.domain.calender.DrinkingDaySummary;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        int totalDrinkingDays,
        BeverageSummary mostConsumedBeverage,
        DrinkingDaySummary mostFrequentDrinkingDay
) {
}
