package com.alc.diary.domain.exception;

public enum ErrorType {
    USER("0000", "유효하지 않은 사용자 데이터");

    private String code;
    private String description;

    ErrorType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
