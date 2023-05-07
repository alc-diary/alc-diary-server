package com.alc.diary.presentation.advice;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.presentation.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ErrorResponse<Void>> domainExceptionHandler(DomainException e) {
        log.error("Error - Code: {}, Message: {}", e.getErrorModel().getCode(), e.getErrorModel().getMessage(), e);
        ErrorResponse<Void> errorResponse =
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getErrorModel().getCode(),
                        e.getErrorModel().getMessage(),
                        null
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<?>> exceptionHandler(Exception e) {
        log.error("Error - Message: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.getDefault(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> validExceptionHandler(MethodArgumentNotValidException e) {
        log.error("Error - Message: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.getDefault(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
    }
}
