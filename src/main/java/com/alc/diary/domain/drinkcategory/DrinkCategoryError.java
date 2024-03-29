package com.alc.diary.domain.drinkcategory;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkCategoryError implements ErrorModel {

    INVALID_INPUT("DC_E0000", "Invalid input parameter."),
    NAME_LENGTH_EXCEED("DC_E0001", "Drink category name length cannot exceed 30 characters."),
    DUPLICATE_NAME("DC_E0002", "Duplicate drink category name."),
    NOT_FOUND("DC_E0004", "Drink category not found."),
    ;

    private final String code;
    private final String message;
}
