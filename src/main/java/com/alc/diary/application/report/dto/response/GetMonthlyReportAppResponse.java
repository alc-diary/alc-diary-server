package com.alc.diary.application.report.dto.response;

import java.time.LocalDateTime;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        float totalBottlesConsumedDiffFromLastMonth,
        int totalDaysDrinking,
        int totalDaysDrinkingDiffFromLastMonth,
        int totalSpentOnDrinks,
        int totalCaloriesFromDrinks,
        int totalRunningTimeToBurnCalories,
        DrinkSummaryDto mostFrequentlyConsumedDrink,
        DrinkingDaySummaryDto mostFrequentlyDrinkingDay,
        String lastDrinkingDateTime
) {
}
