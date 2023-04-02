package com.alc.diary.domain.calender.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalenderError implements ErrorModel {
    NO_ENTITY_FOUND("E0100", "No entity found");

    private final String code;
    private final String message;
}
