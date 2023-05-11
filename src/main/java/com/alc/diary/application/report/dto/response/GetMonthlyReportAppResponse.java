package com.alc.diary.application.report.dto.response;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        float totalBottlesConsumedDiffFromLastMonth,
        int totalDrinkingDaysCount,
        int totalDrinkingDaysCountDiffFromLastMonth,
        BeverageSummaryDto mostConsumedBeverage,
        DrinkingDaySummaryDto mostFrequentDrinkingDay
) {
}
