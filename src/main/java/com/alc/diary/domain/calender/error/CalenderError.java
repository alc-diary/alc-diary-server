package com.alc.diary.domain.calender.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalenderError implements ErrorModel {
    NO_ENTITY_FOUND("E0100", "No entity found"),
    INVALID_PARAMETER_INCLUDE("E0101", "Invalid parameter include"),

    NOT_VALID_USER("E0102", "유효한 사용자 정보를 조회할 수 없습니다.");

    private final String code;
    private final String message;
}
