package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.DrinkModel;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ToString
public class Calenders {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

    public int calculateTotalDaysDrinking() {
        return (int) calenders.stream()
                              .map(calender -> DATE_FORMAT.format(calender.getDrinkStartDateTime()))
                              .distinct()
                              .count();
    }

    public int calculateTotalSpentOnDrinks() {
        return calenders.stream()
                        .flatMapToInt(calender -> calender.getDrinkModels().stream()
                                                          .mapToInt(DrinkModel::getTotalPrice))
                        .sum();
    }

    public int calculateTotalCaloriesFromDrinks() {
        return calenders.stream()
                        .flatMapToInt(calender -> calender.getDrinkModels().stream()
                                                          .mapToInt(DrinkModel::getTotalCalorie))
                        .sum();
    }

    public List<DrinkSummary> calculateMostConsumedDrinkSummaries() {
        Map<DrinkType, Float> totalQuantityByDrinkType = calenders.stream()
                                                                  .flatMap(calender -> calender.getDrinkModels().stream())
                                                                  .collect(Collectors.groupingBy(DrinkModel::getType,
                                                                          Collectors.summingDouble(DrinkModel::getQuantity)))
                                                                  .entrySet().stream()
                                                                  .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().floatValue()));

        final float maxBeverageConsumption = totalQuantityByDrinkType.values().stream()
                                                                     .max(Float::compareTo)
                                                                     .orElse(0.0f);

        return totalQuantityByDrinkType.entrySet().stream()
                                       .filter(entry -> entry.getValue() == maxBeverageConsumption)
                                       .map(entry -> new DrinkSummary(entry.getKey(), entry.getValue()))
                                       .collect(Collectors.toList());
    }

    public List<DrinkingDaySummary> calculateMostFrequentDrinkingDaySummaries() {
        Map<DayOfWeek, Integer> countByDayOfWeek = calenders.stream()
                                                            .map(calender -> calender.getDrinkStartDateTime().getDayOfWeek())
                                                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                            .entrySet().stream()
                                                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().intValue()));

        final int maxDrinkingDayCount = countByDayOfWeek.values().stream()
                                                        .max(Integer::compareTo)
                                                        .orElse(0);

        return countByDayOfWeek.entrySet().stream()
                               .filter(entry -> entry.getValue() == maxDrinkingDayCount)
                               .map(entry -> new DrinkingDaySummary(entry.getKey(), entry.getValue()))
                               .collect(Collectors.toList());
    }

    public Optional<LocalDateTime> lastDrinkingDateTimeOptional() {
        return calenders.stream()
                .map(Calender::getDrinkEndDateTime)
                .max(LocalDateTime::compareTo);
    }
}
