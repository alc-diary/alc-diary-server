package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.model.DrinkModel;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.List;

@ToString
public class Calenders {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<Calender> calenders;

    public Calenders(List<Calender> calenders) {
        this.calenders = calenders;
    }

    public float calculateNumberOfDrinks() {
        return (float) calenders.stream()
                                .flatMapToDouble(calender -> calender.getDrinkModels().stream()
                                                                     .mapToDouble(DrinkModel::getQuantity))
                                .sum();
    }

    public int calculateDaysOfDrinking() {
        return (int) calenders.stream()
                              .map(calender -> dateFormat.format(calender.getDrinkStartDateTime()))
                              .distinct()
                              .count();
    }
}
