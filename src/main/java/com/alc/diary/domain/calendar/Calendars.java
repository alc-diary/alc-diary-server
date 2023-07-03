package com.alc.diary.domain.calendar;

import lombok.ToString;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.*;
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

    public int totalDaysDrinking() {
        return (int) calendars.stream()
                .map(Calendar::getDate)
                .distinct()
                .count();
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

    public List<Calendar> getMostAlcoholConsumedPerDay(long userId) {
        return calendars.stream()
                .collect(Collectors.groupingBy(Calendar::getDate,
                        Collectors.maxBy(comparingByAlcoholConsumed(userId)
                        )))
                .values().stream()
                .flatMap(Optional::stream)
                .toList();
    }

    private static Comparator<Calendar> comparingByAlcoholConsumed(long userId) {
        return Comparator.comparing(calendar ->
                calendar.getUserCalendarByUserId(userId)
                        .map(UserCalendar::getMostConsumedDrink)
                        .map(userCalendarDrink -> userCalendarDrink.map(UserCalendarDrink::getQuantity).orElse(0.0f))
                        .orElse(0.0f));
    }

    public Optional<ZonedDateTime> getLastDrinkingDateTime() {
        return calendars.stream()
                .map(Calendar::getDrinkStartTime)
                .max(ZonedDateTime::compareTo);
    }
}
