package com.alc.diary.domain.calendar;

import com.alc.diary.domain.drink.DrinkType;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ToString
@RequiredArgsConstructor
public class Calendars {

    private final List<Calendar> calendars;
    private final ZoneId zoneId;

    public List<Calendar> getCalendarsByMaxDrinkPerDay(ZoneId zoneId) {
        return calendars.stream()
                .collect(
                        Collectors.groupingBy(
                                calendar -> calendar.getDrinkStartTimeLocalDate(zoneId),
                                Collectors.maxBy(Comparator.comparing(Calendar::getTotalDrinkQuantity))
                        )
                )
                .values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public int totalDaysDrinking() {
        return calendars.stream()
                .collect(
                        Collectors.groupingBy(
                                calendar -> calendar.getDrinkStartTimeLocalDate(zoneId)
                        )
                ).keySet()
                .size();
    }

    public int calculateTotalSpent() {
        return calendars.stream()
                .mapToInt(Calendar::getTotalPrice)
                .sum();
    }

    public int calculateTotalCalories() {
        return calendars.stream()
                .mapToInt(Calendar::getTotalCalories)
                .sum();
    }

    public float calculateTotalQuantity() {
        return (float) calendars.stream()
                .mapToDouble(Calendar::getTotalDrinkQuantity)
                .sum();
    }

    public Optional<DrinkType> mostConsumedDrink() {
        return calendars.stream()
                .flatMap(calendar -> calendar.getUserCalendars().stream())
                .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream())
                .collect(
                        Collectors.groupingBy(
                                DrinkRecord::getType,
                                Collectors.summingDouble(DrinkRecord::getQuantity)
                        )
                ).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<DayOfWeek> mostFrequentDrinkingDay() {
        return calendars.stream()
                .collect(Collectors.groupingBy(
                        calendar -> calendar.getDrinkStartTimeLocalDate(zoneId).getDayOfWeek(),
                        Collectors.counting()
                )).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<ZonedDateTime> getLastDrinkingDateTime() {
        return calendars.stream()
                .map(Calendar::getDrinkEndTime)
                .max(Comparator.naturalOrder());
    }
}
