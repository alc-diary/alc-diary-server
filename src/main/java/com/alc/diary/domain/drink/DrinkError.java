package com.alc.diary.domain.drink;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkError implements ErrorModel {

    NOT_FOUND("DRINK_E0000", "Drink not found"),
    NO_PERMISSION("DRINK_E0001", "No permission"),
    ;

    private final String code;
    private final String message;
}
