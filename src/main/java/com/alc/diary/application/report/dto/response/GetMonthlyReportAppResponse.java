package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.user.enums.AlcoholType;

import java.time.DayOfWeek;

public record GetMonthlyReportAppResponse(

        float totalBottlesConsumed,
        int totalDrinkingDays,
        MostConsumedBeverage mostConsumedBeverage,
        MostFrequentDrinkingDay mostFrequentDrinkingDay
) {

    private record MostConsumedBeverage(
            AlcoholType alcoholType,
            int totalAmount
    ) {
    }

    private record MostFrequentDrinkingDay(
            DayOfWeek dayOfWeek,
            int totalOccurrence
    ) {
    }
}
