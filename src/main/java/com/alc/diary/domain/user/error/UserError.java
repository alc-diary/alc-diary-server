package com.alc.diary.domain.user.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserError implements ErrorModel {

    USER_NOT_FOUND("US_E0000", "User not found."),
    INVALID_PARAMETER_INCLUDE("US_E0001", "Invalid parameter include"),
    DUPLICATE_NICKNAME_TOKEN("US_E0002", "Duplicate nickname token."),
    NICKNAME_ALREADY_TAKEN("US_E0003", "The nickname is already taken."),
    IMAGE_URL_LENGTH_EXCEEDED("US_E0004", "프로필 이미지 URL은 1000자를 초과할 수 없습니다."),
    INVALID_PERSONAL_ALCOHOL_LIMIT("US_E0005", "주량은 0 이상이어야 합니다."),
    INVALID_NON_ALCOHOL_GOAL("US_E0006", "금주 목표일 수는 0 이상 7 이하여야 합니다."),
    INVALID_ALCOHOL_TYPE("US_E0007", "Alcohol Type은 null이 아니어야 합니다."),
    NICKNAME_LENGTH_EXCEEDED("US_E0008", "닉네임은 16자를 초과할 수 없습니다."),
    INVALID_DESCRIPTION_STYLE("US_E0009", "Description Style은 null이 아니어야 합니다."),
    NOT_IN_ONBOARDING_PROCESS("US_E0010", "온보딩 상태의 유저가 아닙니다."),
    INVALID_NICKNAME_FORMAT("US_E0011", "닉네임은 한글, 영어 대소문자, 숫자로만 검색할 수 있습니다."),
    NICKNAME_CONTAINS_BAD_WORD("US_E0012", "The nickname contains prohibited words."),
    ;
    private final String code;
    private final String message;
}
