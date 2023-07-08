package com.alc.diary.domain.drink;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum DrinkType {

    BEER(List.of(DrinkUnit.BOTTLE, DrinkUnit.CAN, DrinkUnit.GLASS, DrinkUnit.ML_500, DrinkUnit.PITCHER), 4000, 408),
    SOJU(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS), 4000, 256),
    WINE(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS), 30000, 548),
    MAKGEOLLI(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS), 4000, 344),
    ;

    private final List<DrinkUnit> allowedUnits;
    private final int price;
    private final int calories;

    public boolean isUnitAllowed(DrinkUnit drinkUnit) {
        return allowedUnits.contains(drinkUnit);
    }
}
