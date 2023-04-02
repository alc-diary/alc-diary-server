package com.alc.diary.domain.exception.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserError implements ErrorModel {

    NO_ENTITY_FOUND("E0000", "No entity found");

    private final String code;
    private final String message;
}
