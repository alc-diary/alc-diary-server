package com.alc.diary.domain.calendar.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkRecordError implements ErrorModel {

    NULL_DRINK_TYPE("DR_E0000", "DrinkType cannot be null."),
    NULL_DRINK_UNIT("DR_E0001", "DrinkUnit cannot be null."),
    INVALID_DRINK_UNIT("DR_E0002", "The provided DrinkUnit is not allowed for the selected DrinkType."),
    ZERO_QUANTITY("DR_E0003", "Quantity cannot be zero."),
    NOT_FOUND("DR_E0004", "DrinkRecord not found."),
    ;

    private final String code;
    private final String message;
}
