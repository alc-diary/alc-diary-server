package com.example.alcdiary.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AlcException extends RuntimeException {

    private final ErrorModel errorModel;
}
