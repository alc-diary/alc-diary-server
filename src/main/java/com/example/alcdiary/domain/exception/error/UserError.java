package com.example.alcdiary.domain.exception.error;

import com.example.alcdiary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError implements ErrorModel {

    NOT_FOUND_USER("Not found user"),

    NO_AUTH_CALENDER_USER("수정 및 삭제권한이 없는 사용자입니다.");

    private final String message;
}
