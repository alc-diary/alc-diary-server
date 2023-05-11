package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;

public record BeverageSummary(DrinkType drinkType, float amount) {

    public static final BeverageSummary EMPTY = new BeverageSummary(null, 0.0f);
}
