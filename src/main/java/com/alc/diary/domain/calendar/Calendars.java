package com.alc.diary.domain.calendar;

import lombok.ToString;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
public class Calendars {

    private final List<Calendar> calendars;

    private Calendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public static Calendars from(List<Calendar> calendars) {
        return new Calendars(calendars);
    }

    public int calculateTotalSpent() {
        return calendars.stream()
                .mapToInt(calendar -> calendar.getUserCalendars().stream().mapToInt(UserCalendar::getTotalPrice).sum())
                .sum();
    }

    public int calculateTotalCalories() {
        return calendars.stream()
                .mapToInt(calendars ->
                        calendars.getUserCalendars().stream().mapToInt(UserCalendar::getTotalCalories).sum())
                .sum();
    }

    public float calculateTotalQuantity() {
        return (float) calendars.stream()
                .mapToDouble(calendars ->
                        calendars.getUserCalendars().stream()
                                .mapToDouble(userCalendar -> userCalendar.getDrinks().stream()
                                        .mapToDouble(UserCalendarDrink::getQuantity).sum())
                                .sum())
                .sum();
    }

    public Optional<UserCalendarDrink> mostConsumedDrink() {
        return calendars.stream()
                .flatMap(calendar -> calendar.getUserCalendars().stream())
                .flatMap(userCalendar -> userCalendar.getDrinks().stream())
                .max(Comparator.comparing(UserCalendarDrink::getQuantity));
    }

    public Optional<DayOfWeek> mostFrequentDrinkingDay() {
        Map<DayOfWeek, Long> dayCounts = calendars.stream()
                .collect(Collectors.groupingBy(Calendar::getDayOfWeek, Collectors.counting()));

        return dayCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
