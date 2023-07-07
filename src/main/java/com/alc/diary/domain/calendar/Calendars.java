package com.alc.diary.domain.calendar;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@ToString
@RequiredArgsConstructor
public class Calendars {

    private final List<Calendar> calendars;

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
}
