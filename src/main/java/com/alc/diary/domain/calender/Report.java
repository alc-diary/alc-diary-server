package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;

import java.time.DayOfWeek;
import java.util.List;

public class Report {

    private final Calenders calenders;

    public Report(List<Calender> calenders) {
        this(new Calenders(calenders));
    }

    public Report(Calenders calenders) {
        this.calenders = calenders;
    }

    public float getNumberOfDrinks() {
        return calenders.calculateBottlesConsumed();
    }

    public int getDaysOfDrinking() {
        return calenders.calculateTotalDrinkingDays();
    }

    public DrinkType getMostDrunkAlcoholType() {
        return calenders.calculateMostConsumedBeverage();
    }

    public List<DayOfWeek> getMostDrunkDayOfWeek() {
        return calenders.calculateMostFrequentDrinkingDay();
    }
}
