package com.alc.diary.application.report.dto.response;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        int totalDrinkingDays,
        BeverageSummaryDto mostConsumedBeverage,
        DrinkingDaySummaryDto mostFrequentDrinkingDay
) {
}
