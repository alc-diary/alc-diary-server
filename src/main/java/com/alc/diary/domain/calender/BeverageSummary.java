package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;

public class BeverageSummary {

    private final DrinkType drinkType;
    private final float totalAmount;

    public BeverageSummary(DrinkType drinkType, float totalAmount) {
        this.drinkType = drinkType;
        this.totalAmount = totalAmount;
    }
}
