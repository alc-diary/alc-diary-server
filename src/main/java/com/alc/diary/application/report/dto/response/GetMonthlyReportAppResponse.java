package com.alc.diary.application.report.dto.response;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        float totalBottlesConsumedDiffFromLastMonth,
        int totalDrinkingDaysCount,
        int totalDrinkingDaysCountDiffFromLastMonth,
        int totalSpendMoney,
        int totalCalories,
        int totalRun,
        BeverageSummaryDto mostConsumedBeverage,
        DrinkingDaySummaryDto mostFrequentDrinkingDay
) {
}
