package com.alc.diary.domain.calendar;

import com.alc.diary.domain.drink.UserCalendarDrink;
import com.alc.diary.domain.usercalendar.UserCalendar;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public List<UserCalendar> getMonthlyCalendar() {
        Map<LocalDate, List<Calendar>> calendarsByLocalDate = calendars.stream()
                .collect(Collectors.groupingBy(Calendar::getLocalDate));
        return calendarsByLocalDate.values().stream()
                .map(it -> it.stream()
                        .flatMap(calendar -> calendar.getUserCalendars().stream())
                        .max(Comparator.comparing(o -> o.getDrinks().stream()
                                .max(UserCalendarDrink::compareTo)
                                .map(UserCalendarDrink::getQuantity)
                                .orElse(0.0f)))
                        .orElse(null))
                .toList();
    }
}
