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
        return calenders.calculateNumberOfDrinks();
    }

    public int getDaysOfDrinking() {
        return calenders.calculateDaysOfDrinking();
    }

    public DrinkType getMostDrunkAlcoholType() {
        return calenders.getMostDrunkAlcoholType();
    }

    public List<DayOfWeek> getMostDrunkDayOfWeek() {
        return calenders.getMostDrunkDayOfWeek();
    }
}
