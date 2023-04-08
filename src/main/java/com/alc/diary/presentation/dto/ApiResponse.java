package com.alc.diary.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> getSuccess(T data) {
        ApiResponse<T> objectApiResponse = new ApiResponse<>();
        objectApiResponse.status = HttpStatus.OK.value();
        objectApiResponse.code = "S0000";
        objectApiResponse.message = "success";
        objectApiResponse.data = data;
        return objectApiResponse;
    }
}
