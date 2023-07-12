package com.alc.diary.domain.calendar.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoError implements ErrorModel {

    NO_PERMISSION_TO_DELETE_PHOTO("P_E0001", "You do not have permission to delete this photo"),
    ;

    private final String code;
    private final String message;
}
