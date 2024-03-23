package com.alc.diary.domain.customerrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceSatisfactionLevel {

    EXCELLENT("매우 좋아"),
    GOOD("좋아"),
    AVERAGE("보통이야"),
    BELOW_AVERAGE("별로야"),
    POOR("매우 별로야"),
    ;

    private final String description;
}
