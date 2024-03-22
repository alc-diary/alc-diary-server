package com.alc.diary.domain.customerrequest;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerRequestError implements ErrorModel {

    NOT_FOUND("CUSTOMER_E0000", "CustomerRequest entity not found"),;

    private final String code;
    private final String message;
}
