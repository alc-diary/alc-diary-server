package com.alc.diary.domain.calendar;

import lombok.ToString;

import java.util.List;

@ToString
public class Calendars {

    private final List<Calendar> calendars;

    private Calendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public static Calendars from(List<Calendar> calendars) {
        return new Calendars(calendars);
    }

//    public List<UserCalendar> getMonthlyCalendar() {
//        Map<LocalDate, List<Calendar>> calendarsByLocalDate = calendars.stream()
//                .collect(Collectors.groupingBy(Calendar::getLocalDate));
//        return calendarsByLocalDate.values().stream()
//                .map(it -> it.stream()
//                        .flatMap(calendar -> calendar.getUserCalendars().stream())
//                        .max(Comparator.comparing(o -> o.getDrinks().stream()
//                                .max(UserCalendarDrink::compareTo)
//                                .map(UserCalendarDrink::getQuantity)
//                                .orElse(0.0f)))
//                        .orElse(null))
//                .toList();
//    }
}
