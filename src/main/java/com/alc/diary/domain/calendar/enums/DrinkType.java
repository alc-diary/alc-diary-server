package com.alc.diary.domain.calendar.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum DrinkType {

    BEER(List.of(DrinkUnitType.BOTTLE, DrinkUnitType.CAN, DrinkUnitType.GLASS, DrinkUnitType.ML_500, DrinkUnitType.PITCHER), 4000, 408),
    SOJU(List.of(DrinkUnitType.BOTTLE, DrinkUnitType.GLASS), 4000, 256),
    WINE(List.of(DrinkUnitType.BOTTLE, DrinkUnitType.GLASS), 30000, 548),
    MAKGEOLLI(List.of(DrinkUnitType.BOTTLE, DrinkUnitType.GLASS), 4000, 344),
    ;

    private final List<DrinkUnitType> allowedUnits;
    private final int price;
    private final int calories;

    public boolean isUnitAllowed(DrinkUnitType drinkUnitType) {
        return allowedUnits.contains(drinkUnitType);
    }
}
