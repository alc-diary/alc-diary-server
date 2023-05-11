package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.DrinkingDaySummary;

import java.time.DayOfWeek;

public record DrinkingDaySummaryDto(

        DayOfWeek dayOfWeek,
        int totalOccurrence
) {

    public DrinkingDaySummaryDto(DrinkingDaySummary drinkingDaySummary) {
        this(drinkingDaySummary.dayOfWeek(), drinkingDaySummary.totalOccurrence());
    }
}
