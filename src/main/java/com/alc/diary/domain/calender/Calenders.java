package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.model.DrinkModel;

import java.util.List;

public class Calenders {

    private final List<Calender> calenders;

    public Calenders(List<Calender> calenders) {
        this.calenders = calenders;
    }

    public float getNumberOfDrinks() {
        return (float) calenders.stream()
                        .flatMapToDouble(calender -> calender.getDrinkModels().stream()
                                                             .mapToDouble(DrinkModel::getQuantity))
                        .sum();
    }

    public double getDaysOfDrinking() {

    }
}
