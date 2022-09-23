package com.example.alcdiary.presentation.advice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseModel<T> {
    private Boolean success;
    private String message;
    private T data;


    public ApiResponseModel(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
