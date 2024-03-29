package com.alc.diary.domain.report;

import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calendar.enums.DrinkType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class Report {

    private static final int CALORIES_BURN_PER_HOUR_RUNNING = 580;
    private static final int PRICE_OF_A_BOWL_OF_RICE_SOUP = 8000;

    public final Calendars calendars;

    public int totalDaysDrinking() {
        return calendars.totalDaysDrinking();
    }

    public int totalSpentOnDrinks() {
        return calendars.calculateTotalSpent();
    }

    public int totalCalories() {
        return calendars.calculateTotalCalories();
    }

    public float totalQuantity() {
        return calendars.calculateTotalQuantity();
    }

    public int totalRunningTimeToBurnCalories() {
        return Math.round((float) totalCalories() / CALORIES_BURN_PER_HOUR_RUNNING);
    }

    public int riceSoupEquivalent() {
        return Math.round((float) totalSpentOnDrinks() / PRICE_OF_A_BOWL_OF_RICE_SOUP);
    }

    public Optional<DrinkType> mostConsumedDrink() {
        return calendars.mostConsumedDrink();
    }

    public Optional<DayOfWeek> mostFrequentDrinkingDay() {
        return calendars.mostFrequentDrinkingDay();
    }

    public Optional<ZonedDateTime> getLastDrinkingDateTime() {
        return calendars.getLastDrinkingDateTime();
    }
}
