package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.UserCalendarDrink;
import com.alc.diary.domain.usercalendar.UserCalendar;

import java.util.List;

public record GetMonthlyCalendarsAppResponse(

        List<MonthlyCalendar> monthlyCalendars
) {

    public static GetMonthlyCalendarsAppResponse from(List<UserCalendar> userCalendars) {
        return new GetMonthlyCalendarsAppResponse(userCalendars.stream()
                .map(userCalendar -> new MonthlyCalendar(userCalendar.getCalendar().getLocalDate().toString(),
                        userCalendar.getDrinks().stream().max(UserCalendarDrink::compareTo).map(UserCalendarDrink::getDrink).orElse(null)
                ))
                .toList()
        );
    }

    private record MonthlyCalendar(

            String drinkDate,
            Drink drink
    ) {
    }
}
