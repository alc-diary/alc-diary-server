package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.enums.DrinkType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GetMonthlyCalendarsResponse(

        String date,
        DrinkType drinkType,
        boolean drinkRecorded
) {

    public static List<GetMonthlyCalendarsResponse> fromDomainEntity(Calendars calendars, long userId) {
        return calendars.getCalendarsPerDay().entrySet().stream()
                .map(localDateListEntry -> {
                    List<Calendar> value = localDateListEntry.getValue();
                    Map<DrinkType, Double> drinkSumByType = value.stream()
                            .flatMap(calendar -> calendar.getUserCalendars().stream())
                            .filter(userCalendar -> userCalendar.isOwner(userId))
                            .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream())
                            .collect(Collectors.groupingBy(
                                    DrinkRecord::getType,
                                    Collectors.summingDouble(DrinkRecord::getQuantity)
                            ));
                    Map<LocalDate, DrinkType> result = new HashMap<>();
                    result.put(
                            localDateListEntry.getKey(),
                            drinkSumByType.entrySet().stream()
                                    .max(Map.Entry.comparingByValue())
                                    .map(Map.Entry::getKey)
                                    .orElse(null));
                    return result;
                })
                .flatMap(map -> map.entrySet().stream())
                .map(entry -> new GetMonthlyCalendarsResponse(
                        entry.getKey().toString(),
                        entry.getValue(),
                        true
                ))
                .sorted(Comparator.comparing(GetMonthlyCalendarsResponse::date))
                .toList();
    }
}
