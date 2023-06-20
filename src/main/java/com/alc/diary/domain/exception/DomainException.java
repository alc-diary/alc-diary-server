package com.alc.diary.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class DomainException extends RuntimeException {

    private final ErrorModel errorModel;
    private final String additionalInfo;

    public DomainException(ErrorModel errorModel) {
        this(errorModel, null);
    }

    public DomainException(ErrorModel errorModel, String additionalInfo) {
        super(errorModel.getMessage());
        this.errorModel = errorModel;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String getMessage() {
        if (additionalInfo != null) {
            return errorModel.getMessage() + " | " + additionalInfo;
        }
        return errorModel.getMessage();
    }
}