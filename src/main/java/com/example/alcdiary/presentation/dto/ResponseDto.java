package com.example.alcdiary.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    public ResponseDto(T data) {
        this("success", data);
    }

    public ResponseDto(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    private Boolean success;
    private String message;
    private T data;

    public ResponseEntity<ResponseDto<T>> toResponseEntity() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseDto<>(
                                success,
                                message,
                                data
                        )
                );
    }
}
