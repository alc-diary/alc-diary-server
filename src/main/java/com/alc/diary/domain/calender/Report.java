package com.alc.diary.domain.calender;

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
}
