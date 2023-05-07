package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.DrinkModel;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public DrinkType getMostDrunkAlcoholType() {
        Map<DrinkType, List<DrinkModel>> drinkModelsByDrinkType = calenders.stream()
                                                                           .flatMap(calender -> calender.getDrinkModels().stream())
                                                                           .collect(Collectors.groupingBy(DrinkModel::getType));
        float maxNumberOfDrinks = 0;
        DrinkType mostDrunkAlcoholType = null;
        for (DrinkType type : drinkModelsByDrinkType.keySet()) {
            float sumOfNumberOfDrinks = (float) drinkModelsByDrinkType.get(type).stream()
                                               .mapToDouble(DrinkModel::getQuantity)
                                               .sum();
            if (maxNumberOfDrinks <= sumOfNumberOfDrinks) {
                maxNumberOfDrinks = sumOfNumberOfDrinks;
                mostDrunkAlcoholType = type;
            }
        }
        return mostDrunkAlcoholType;
    }
}
