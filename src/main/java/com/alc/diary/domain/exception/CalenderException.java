package com.alc.diary.domain.exception;

public class CalenderException extends DomainException {
    public CalenderException(ErrorModel errorModel) {
        super(errorModel);
    }
}