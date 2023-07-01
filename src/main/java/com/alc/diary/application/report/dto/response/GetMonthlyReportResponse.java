package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.report.Report;

import java.time.DayOfWeek;

public record GetMonthlyReportResponse(
        int totalSpentOnDrinks,
        int totalCalories,
        float totalQuantity,
        int totalRunningTime,
        int riceSoupEquivalent,
        DrinkDto mostConsumedDrink,
        DayOfWeek mostFrequentDrinkingDay
) {

    public static GetMonthlyReportResponse from(Report report) {
        return new GetMonthlyReportResponse(
                report.totalSpentOnDrinks(),
                report.totalCalories(),
                report.totalQuantity(),
                report.totalRunningTimeToBurnCalories(),
                report.riceSoupEquivalent(),
                report.mostConsumedDrink().map(userCalendarDrink ->
                                new GetMonthlyReportResponse.DrinkDto(
                                        userCalendarDrink.getDrinkUnitInfoId(),
                                        userCalendarDrink.getQuantity())
                        )
                        .orElse(null),
                report.mostFrequentDrinkingDay().orElse(null)
        );
    }

    public record DrinkDto(
            long drinkUnitInfoId,
            float quantity
    ) {
    }
}
