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

    public float totalDrinkQuantity() {
        return calenders.calculateTotalDrinkQuantity();
    }

    public int totalDrinkingDays() {
        return calenders.calculateTotalDrinkingDays();
    }

    public int totalSpendMoney() {
        return calenders.calculateTotalSpendMoney();
    }

    public int totalCalories() {
        return calenders.calculateTotalCalories();
    }

    public BeverageSummary mostConsumedBeverageSummary() {
        return calenders.calculateMostConsumedBeverageSummaries().stream()
                .findFirst()
                .orElse(BeverageSummary.EMPTY);
    }

    public DrinkingDaySummary mostFrequentDrinkingDaySummary() {
        return calenders.calculateMostFrequentDrinkingDaySummaries().stream()
                .findFirst()
                .orElse(DrinkingDaySummary.EMPTY);
    }
}
