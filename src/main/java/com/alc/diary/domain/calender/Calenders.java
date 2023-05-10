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

    public float calculateTotalDrinkQuantity() {
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

    public BeverageSummary calculateMostConsumedBeverageSummary() {
        Map<DrinkType, List<DrinkModel>> drinkModelsByDrinkType = calenders.stream()
                                                                           .flatMap(calender -> calender.getDrinkModels().stream())
                                                                           .collect(Collectors.groupingBy(DrinkModel::getType));
        float maxBeverageConsumption = 0;
        DrinkType mostConsumedDrinkType = null;
        for (DrinkType type : drinkModelsByDrinkType.keySet()) {
            float sumOfNumberOfDrinks = (float) drinkModelsByDrinkType.get(type).stream()
                                               .mapToDouble(DrinkModel::getQuantity)
                                               .sum();
            if (maxBeverageConsumption <= sumOfNumberOfDrinks) {
                maxBeverageConsumption = sumOfNumberOfDrinks;
                mostConsumedDrinkType = type;
            }
        }
        return new BeverageSummary(mostConsumedDrinkType, maxBeverageConsumption);
    }

    public DrinkingDaySummary calculateMostFrequentDrinkingDaySummary() {
        Map<DayOfWeek, Long> countByDayOfWeek = calenders.stream()
                                                .map(calender -> calender.getDrinkStartDateTime().getDayOfWeek())
                                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        int maxDrinkingDayCount = 0;
        List<DayOfWeek> mostFrequentDrinkingDays = new ArrayList<>();
        for (DayOfWeek dayOfWeek : countByDayOfWeek.keySet()) {
            if (maxDrinkingDayCount < countByDayOfWeek.get(dayOfWeek)) {
                mostFrequentDrinkingDays.clear();
                maxDrinkingDayCount = countByDayOfWeek.get(dayOfWeek).intValue();
                mostFrequentDrinkingDays.add(dayOfWeek);
            } else if (maxDrinkingDayCount == countByDayOfWeek.get(dayOfWeek)) {
                mostFrequentDrinkingDays.add(dayOfWeek);
            }
        }
        return new DrinkingDaySummary(mostFrequentDrinkingDays.get(0), maxDrinkingDayCount);
    }
}
