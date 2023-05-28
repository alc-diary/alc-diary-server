package com.alc.diary.domain.calender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Report {

    private static final int CALORIES_BURN_PER_HOUR_RUNNING = 580;
    private static final int PRICE_OF_A_BOWL_OF_RICE_SOUP = 8000;
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
        return calenders.calculateTotalDaysDrinking();
    }

    public int totalSpentOnDrinks() {
        return calenders.calculateTotalSpentOnDrinks();
    }

    public int totalRunningTimeToBurnCalories() {
        return Math.round((float) calenders.calculateTotalCaloriesFromDrinks() / CALORIES_BURN_PER_HOUR_RUNNING);
    }

    public int totalCaloriesFromDrinks() {
        return calenders.calculateTotalCaloriesFromDrinks();
    }

    public int riceSoupEquivalent() {
        return Math.round((float) totalSpentOnDrinks() / PRICE_OF_A_BOWL_OF_RICE_SOUP);
    }

    public DrinkSummary mostConsumedDrinkSummary() {
        return calenders.calculateMostConsumedDrinkSummaries().stream()
                        .findFirst()
                        .orElse(DrinkSummary.EMPTY);
    }

    public DrinkingDaySummary mostFrequentDrinkingDaySummary() {
        return calenders.calculateMostFrequentDrinkingDaySummaries().stream()
                .findFirst()
                .orElse(DrinkingDaySummary.EMPTY);
    }

    public float calculateDrinkQuantityDifference(Report otherReport) {
        return totalDrinkQuantity() - otherReport.totalDrinkQuantity();
    }

    public int calculateTotalDrinkingDaysDifference(Report otherReport) {
        return totalDrinkingDays() - otherReport.totalDrinkingDays();
    }

    public Optional<LocalDateTime> lastDrinkingDateTimeOptional() {
        return calenders.lastDrinkingDateTimeOptional();
    }
}
