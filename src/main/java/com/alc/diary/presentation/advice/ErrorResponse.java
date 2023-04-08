package com.alc.diary.presentation.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse<T> {

    private int status;
    private String code;
    private String message;
    private T data;

    public static ErrorResponse getDefault() {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "E9999", "Internal server error", null);
    }
}
