package com.alc.diary.domain.categoryunit;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryUnitError implements ErrorModel {

    NOT_FOUND("CU_E0000", "Category unit not found."),
    ;

    private final String code;
    private final String message;
}
