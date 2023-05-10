package com.alc.diary.domain.calender;

import java.time.DayOfWeek;

public class DrinkingDaySummary {

    private final DayOfWeek dayOfWeek;
    private final int totalOccurrence;

    public DrinkingDaySummary(DayOfWeek dayOfWeek, int totalOccurrence) {
        this.dayOfWeek = dayOfWeek;
        this.totalOccurrence = totalOccurrence;
    }
}
