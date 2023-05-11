package com.alc.diary.domain.calender;

import java.time.DayOfWeek;

public record DrinkingDaySummary(DayOfWeek dayOfWeek, int totalDrinkingDaysCount) {

    public static final DrinkingDaySummary EMPTY = new DrinkingDaySummary(null, 0);
}
