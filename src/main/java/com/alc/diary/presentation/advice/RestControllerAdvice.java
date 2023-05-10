package com.alc.diary.presentation.advice;

import com.alc.diary.domain.exception.CalenderException;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.presentation.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Logger slackLogger = LoggerFactory.getLogger("SLACK");

    @ExceptionHandler(value = {DomainException.class, CalenderException.class})
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
        slackLogger.error("Error - Message: {}", e.getMessage(), e);
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
