package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.DrinkModel;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ToString
public class Calenders {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final List<Calender> calenders;

    public Calenders(List<Calender> calenders) {
        this.calenders = calenders;
    }

    public float calculateBottlesConsumed() {
        return (float) calenders.stream()
                                .flatMapToDouble(calender -> calender.getDrinkModels().stream()
                                                                     .mapToDouble(DrinkModel::getQuantity))
                                .sum();
    }

    public int calculateTotalDrinkingDays() {
        return (int) calenders.stream()
                              .map(calender -> dateFormat.format(calender.getDrinkStartDateTime()))
                              .distinct()
                              .count();
    }

    public DrinkType calculateMostConsumedBeverage() {
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

    public List<DayOfWeek> calculateMostFrequentDrinkingDay() {
        Map<DayOfWeek, Long> countByDayOfWeek = calenders.stream()
                                                .map(calender -> calender.getDrinkStartDateTime().getDayOfWeek())
                                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        int maxDrunkDayOfWeekCount = 0;
        List<DayOfWeek> mostDrunkDayOfWeek = new ArrayList<>();
        for (DayOfWeek dayOfWeek : countByDayOfWeek.keySet()) {
            if (maxDrunkDayOfWeekCount < countByDayOfWeek.get(dayOfWeek)) {
                mostDrunkDayOfWeek.clear();
                mostDrunkDayOfWeek.add(dayOfWeek);
            } else if (maxDrunkDayOfWeekCount == countByDayOfWeek.get(dayOfWeek)) {
                mostDrunkDayOfWeek.add(dayOfWeek);
            }
        }
        return mostDrunkDayOfWeek;
    }
}
