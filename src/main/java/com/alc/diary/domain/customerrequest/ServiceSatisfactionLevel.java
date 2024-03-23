package com.alc.diary.domain.customerrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceSatisfactionLevel {

    EXCELLENT("매우 만족"),
    GOOD("만족"),
    AVERAGE("보통"),
    BELOW_AVERAGE("불만족"),
    POOR("매우 불만족"),
    ;

    private final String description;
}
