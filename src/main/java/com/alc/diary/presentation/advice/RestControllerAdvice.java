package com.alc.diary.presentation.advice;

import com.alc.diary.domain.exception.CalenderException;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.presentation.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {


    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Logger slackLogger = LoggerFactory.getLogger("SLACK");

    @ExceptionHandler(value = {DomainException.class, CalenderException.class})
    public ResponseEntity<ErrorResponse<Void>> domainExceptionHandler(HttpServletRequest request, DomainException e) {
        log.error("Error occurred at {} {} from {} - Code: {}, Message: {}, Class: {}, Method: {}, Line: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                e.getErrorModel().getCode(),
                e.getErrorModel().getMessage(),
                e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(),
                e.getStackTrace()[0].getLineNumber()
        );
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> validExceptionHandler(MethodArgumentNotValidException e) {
        log.error("Error - Message: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.internalServerError(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse<?>> servletRequestBindingExceptionHandler(HttpServletRequest request, ServletRequestBindingException e) {
        log.error("Error occurred at {} {} from {} - Code: {}, Message: {}, Class: {}, Method: {}, Line: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                "E9998",
                e.getMessage(),
                e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(),
                e.getStackTrace()[0].getLineNumber()
        );
        ErrorResponse<Void> errorResponse = new ErrorResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "E9998",
                "Invalid request parameters",
                null
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<?>> exceptionHandler(HttpServletRequest request, Exception e) {
        slackLogger.error("Unexpected error occurred at {} {} from {}: {}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.internalServerError("An unexpected error occurred. Please try again later."));
    }
}
