package com.alc.diary.domain.drinkunit;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkUnitError implements ErrorModel {

    INVALID_INPUT("DU_E0000", "Invalid input parameter."),
    NAME_LENGTH_EXCEED("DU_E0001", "Drink unit name length cannot exceed 30 characters."),
    DUPLICATE_NAME("DU_E0002", "Duplicate drink unit name."),
    ;

    private final String code;
    private final String message;
}
