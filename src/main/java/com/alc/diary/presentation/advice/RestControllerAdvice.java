package com.alc.diary.presentation.advice;

import com.alc.diary.domain.exception.DomainException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(value = DomainException.class)
    void errorHandler(DomainException exception) {
        // TODO
    }
}
