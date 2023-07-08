package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.drink.DrinkType;
import com.alc.diary.domain.report.Report;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

public record GetMonthlyReportResponse(

        float totalBottlesConsumed,
        float totalBottlesConsumedDiffFromLastMonth,
        int totalDaysDrinking,
        int totalDaysDrinkingDiffFromLastMonth,
        int totalSpentOnDrinks,
        int totalCaloriesFromDrinks,
        int totalRunningTimeToBurnCalories,
        int riceSoupEquivalent,
        DrinkType mostFrequentlyConsumedDrink,
        DayOfWeek mostFrequentlyDrinkingDay,
        String lastDrinkingDate
) {

    public static GetMonthlyReportResponse from(Report report, Report lastMonthReport) {
       return new GetMonthlyReportResponse(
               report.totalQuantity(),
               report.totalQuantity() - lastMonthReport.totalQuantity(),
               report.totalDaysDrinking(),
               report.totalDaysDrinking() - lastMonthReport.totalDaysDrinking(),
               report.totalSpentOnDrinks(),
               report.totalCalories(),
               report.totalRunningTimeToBurnCalories(),
               report.riceSoupEquivalent(),
               report.mostConsumedDrink().orElse(null),
               report.mostFrequentDrinkingDay().orElse(null),
               report.getLastDrinkingDateTime().map(DateTimeFormatter.ISO_OFFSET_DATE_TIME::format).orElse(null)
       );
    }
}
