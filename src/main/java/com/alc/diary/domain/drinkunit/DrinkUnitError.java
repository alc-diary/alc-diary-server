package com.alc.diary.domain.drinkunit;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkUnitError implements ErrorModel {

    NOT_FOUND("DRINK_UNIT_E0000", "Drink unit not found"),
    INVALID_INPUT("DRINK_UNIT_E0001", "Invalid input parameter."),
    NAME_LENGTH_EXCEED("DRINK_UNIT_E0002", "Drink unit name length cannot exceed 30 characters."),
    DUPLICATE_NAME("DRINK_UNIT_E0003", "Duplicate drink unit name."),
    ;

    private final String code;
    private final String message;
}
