package com.example.alcdiary.domain.exception.error;

import com.example.alcdiary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalenderError implements ErrorModel {

    NOT_FOUND_CALENDER("Not found calender"),
    COULD_NOT_SAVE("캘린더 저장 중 오류가 발생하였습니다.");

    DELETE_ERROR_CALENDER("삭제 도중 에러가 발생하였습니다.");
    private final String message;
}
