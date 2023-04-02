package com.alc.diary.domain.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainException extends RuntimeException {
    private final ErrorModel errorModel;
}