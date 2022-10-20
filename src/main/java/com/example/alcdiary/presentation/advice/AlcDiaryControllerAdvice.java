package com.example.alcdiary.presentation.advice;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlcDiaryControllerAdvice {

    @ExceptionHandler(AlcException.class)
    public ResponseEntity<ErrorResponse> AlcExceptionHandler(AlcException e) {
        ErrorModel errorModel = e.getErrorModel();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorModel.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> ExceptionHandler(Exception e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage()));
    }
}
