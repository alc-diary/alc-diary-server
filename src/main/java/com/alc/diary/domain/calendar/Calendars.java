package com.alc.diary.domain.calendar;

import lombok.ToString;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ToString
public class Calendars {
//
//    private final List<CalendarLegacy> calendarLagacies;
//
//    private Calendars(List<CalendarLegacy> calendarLagacies) {
//        this.calendarLagacies = calendarLagacies;
//    }
//
//    public static Calendars from(List<CalendarLegacy> calendarLagacies) {
//        return new Calendars(calendarLagacies);
//    }
//
//    public int totalDaysDrinking() {
//        return (int) calendarLagacies.stream()
//                .map(CalendarLegacy::getDate)
//                .distinct()
//                .count();
//    }
//
//    public int calculateTotalSpent() {
//        return calendarLagacies.stream()
//                .mapToInt(calendar -> calendar.getUserCalendarLagecies().stream().mapToInt(UserCalendarLegacy::getTotalPrice).sum())
//                .sum();
//    }
//
//    public int calculateTotalCalories() {
//        return calendarLagacies.stream()
//                .mapToInt(calendars ->
//                        calendars.getUserCalendarLagecies().stream().mapToInt(UserCalendarLegacy::getTotalCalories).sum())
//                .sum();
//    }
//
//    public float calculateTotalQuantity() {
//        return (float) calendarLagacies.stream()
//                .mapToDouble(calendars ->
//                        calendars.getUserCalendarLagecies().stream()
//                                .mapToDouble(userCalendar -> userCalendar.getDrinks().stream()
//                                        .mapToDouble(UserCalendarDrink::getQuantity).sum())
//                                .sum())
//                .sum();
//    }
//
//    public Optional<UserCalendarDrink> mostConsumedDrink() {
//        return calendarLagacies.stream()
//                .flatMap(calendar -> calendar.getUserCalendarLagecies().stream())
//                .flatMap(userCalendar -> userCalendar.getDrinks().stream())
//                .max(Comparator.comparing(UserCalendarDrink::getQuantity));
//    }
//
//    public Optional<DayOfWeek> mostFrequentDrinkingDay() {
//        Map<DayOfWeek, Long> dayCounts = calendarLagacies.stream()
//                .collect(Collectors.groupingBy(CalendarLegacy::getDayOfWeek, Collectors.counting()));
//
//        return dayCounts.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey);
//    }
//
//    public List<CalendarLegacy> getMostAlcoholConsumedPerDay(long userId) {
//        return calendarLagacies.stream()
//                .collect(Collectors.groupingBy(CalendarLegacy::getDate,
//                        Collectors.maxBy(comparingByAlcoholConsumed(userId)
//                        )))
//                .values().stream()
//                .flatMap(Optional::stream)
//                .toList();
//    }
//
//    private static Comparator<CalendarLegacy> comparingByAlcoholConsumed(long userId) {
//        return Comparator.comparing(calendar ->
//                calendar.getUserCalendarByUserId(userId)
//                        .map(UserCalendarLegacy::getMostConsumedDrink)
//                        .map(userCalendarDrink -> userCalendarDrink.map(UserCalendarDrink::getQuantity).orElse(0.0f))
//                        .orElse(0.0f));
//    }
//
//    public Optional<ZonedDateTime> getLastDrinkingDateTime() {
//        return calendarLagacies.stream()
//                .map(CalendarLegacy::getDrinkStartTime)
//                .max(ZonedDateTime::compareTo);
//    }
}
