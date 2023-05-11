package com.alc.diary.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse<T> {

    private int status;
    private String code;
    private String message;
    private T data;

    public static <T> ErrorResponse<T> internalServerError(T data) {
        return new ErrorResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "E9999",
            "Internal server error",
            data
        );
    }
}
