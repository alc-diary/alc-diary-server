package com.alc.diary.application.report.dto.response;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        float totalBottlesConsumedDiffFromLastMonth,
        int totalDaysDrinking,
        int totalDaysDrinkingDiffFromLastMonth,
        int totalSpentOnDrinks,
        int totalCaloriesFromDrinks,
        int totalRunningTimeToBurnCalories,
        int riceSoupEquivalent,
        DrinkSummaryDto mostFrequentlyConsumedDrink,
        DrinkingDaySummaryDto mostFrequentlyDrinkingDay,
        String lastDrinkingDateTime
) {
}
