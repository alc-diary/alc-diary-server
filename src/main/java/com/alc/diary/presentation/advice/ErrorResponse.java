package com.alc.diary.presentation.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

    public static ErrorResponse getDefault() {
        return new ErrorResponse("E9999", "Internal server error");
    }
}
