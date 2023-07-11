package com.alc.diary.domain.calendar.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkUnit {

    BOTTLE("병"),
    GLASS("잔"),
    CAN("캔"),
    ML_500("500mL"),
    PITCHER("피처"),
    ;

    private final String displayName;
}
