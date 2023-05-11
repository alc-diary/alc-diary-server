package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.DrinkingDaySummary;

import java.time.DayOfWeek;

public record DrinkingDaySummaryDto(

        DayOfWeek dayOfWeek,
        int totalDrinkingDaysCount,
        int drinkingDaysCountDiffFromLastMonth
) {

    public DrinkingDaySummaryDto(
            DrinkingDaySummary currentMonthDrinkingDaySummary,
            DrinkingDaySummary lastMonthDrinkingDaySummary
    ) {
        this(
                currentMonthDrinkingDaySummary.dayOfWeek(),
                currentMonthDrinkingDaySummary.totalDrinkingDaysCount(),
                currentMonthDrinkingDaySummary.totalDrinkingDaysCount() - lastMonthDrinkingDaySummary.totalDrinkingDaysCount()
        );
    }
}
