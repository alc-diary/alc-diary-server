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
public class ApiResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public static ApiResponse<Void> getSuccess() {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.status = HttpStatus.OK.value();
        apiResponse.code = "S0000";
        apiResponse.message = "success";
        return apiResponse;
    }

    public static <T> ApiResponse<T> getSuccess(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.status = HttpStatus.OK.value();
        apiResponse.code = "S0000";
        apiResponse.message = "success";
        apiResponse.data = data;
        return apiResponse;
    }

    public static ApiResponse<Void> getCreated() {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.status = HttpStatus.CREATED.value();
        apiResponse.code = "S0001";
        apiResponse.message = "created";
        return apiResponse;
    }
}
