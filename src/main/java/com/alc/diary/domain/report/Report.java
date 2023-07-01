package com.alc.diary.domain.report;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calendar.UserCalendarDrink;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public class Report {

    private static final int CALORIES_BURN_PER_HOUR_RUNNING = 580;
    private static final int PRICE_OF_A_BOWL_OF_RICE_SOUP = 8000;

    public final Calendars calendars;

    public Report(List<Calendar> calendars) {
        this(Calendars.from(calendars));
    }

    public Report(Calendars calendars) {
        this.calendars = calendars;
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
        return Math.round((float) totalCalories() / PRICE_OF_A_BOWL_OF_RICE_SOUP);
    }

    public Optional<UserCalendarDrink> mostConsumedDrink() {
        return calendars.mostConsumedDrink();
    }

    public Optional<DayOfWeek> mostFrequentDrinkingDay() {
        return calendars.mostFrequentDrinkingDay();
    }
}
