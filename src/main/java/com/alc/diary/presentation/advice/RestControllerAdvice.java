package com.alc.diary.presentation.advice;

import com.alc.diary.domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ErrorResponse> domainExceptionHandler(DomainException exception) {
        ErrorResponse errorResponse =
            new ErrorResponse(exception.getErrorModel().getCode(), exception.getErrorModel().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error("Error message: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.getDefault());
    }
}
