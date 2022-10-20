package com.example.alcdiary.domain.exception.error;

import com.example.alcdiary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalenderError implements ErrorModel {

    NOT_FOUND_CALENDER("Not found calender");

    private final String message;
}
