package com.alc.diary.domain.drink;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCalendarDrinkError implements ErrorModel {

    USER_CALENDAR_DRINK_NOT_FOUND("UCD_E0000", "The specified UserCalendarDrink entity could not be found."),
    DRINK_NULL("UCD_E0001", "Drink cannot be null."),
    INVALID_QUANTITY("UCD_E0002", "Quantity must be greater then 0."),
    ;

    private final String code;
    private final String message;
}
