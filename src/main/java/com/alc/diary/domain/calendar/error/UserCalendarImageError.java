package com.alc.diary.domain.calendar.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCalendarImageError implements ErrorModel {

    IMAGE_LIMIT_EXCEEDED("UCI_E0000", "The number of images cannot exceed 5."),
    ;

    private final String code;
    private final String message;
}
