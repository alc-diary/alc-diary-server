package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;

public record DrinkSummary(DrinkType drinkType, float totalQuantity) {

    public static final DrinkSummary EMPTY = new DrinkSummary(null, 0.0f);
}
