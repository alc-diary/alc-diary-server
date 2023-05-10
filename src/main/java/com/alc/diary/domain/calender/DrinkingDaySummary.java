package com.alc.diary.domain.calender;

import lombok.Getter;

import java.time.DayOfWeek;

@Getter
public class DrinkingDaySummary {

    private final DayOfWeek dayOfWeek;
    private final int totalOccurrence;

    public DrinkingDaySummary(DayOfWeek dayOfWeek, int totalOccurrence) {
        this.dayOfWeek = dayOfWeek;
        this.totalOccurrence = totalOccurrence;
    }
}
