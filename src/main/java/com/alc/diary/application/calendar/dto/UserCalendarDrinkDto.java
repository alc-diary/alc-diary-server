package com.alc.diary.application.calendar.dto;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.UserCalendarDrink;

import java.util.List;
import java.util.Set;
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
