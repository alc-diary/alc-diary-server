package com.alc.diary.application.calendar.dto;

import java.util.List;
import java.util.stream.Collectors;

public record UserCalendarDrinkDto(

        Long id,
        Long userCalendarId,
        Drink drink,
        float quantity
) {

    public static List<UserCalendarDrinkDto> from(List<UserCalendarDrink> userCalendarDrinks) {
        return userCalendarDrinks.stream()
                .map(userCalendarDrink -> new UserCalendarDrinkDto(
                        userCalendarDrink.getId(),
                        userCalendarDrink.getUserCalendar().getId(),
                        userCalendarDrink.getDrink(),
                        userCalendarDrink.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
