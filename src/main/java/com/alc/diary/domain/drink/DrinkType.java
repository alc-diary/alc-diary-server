package com.alc.diary.domain.drink;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum DrinkType {

    BEER(List.of(DrinkUnit.BOTTLE, DrinkUnit.CAN, DrinkUnit.GLASS, DrinkUnit.ML_500, DrinkUnit.PITCHER)),
    SOJU(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS)),
    WINE(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS)),
    MAKGEOLLI(List.of(DrinkUnit.BOTTLE, DrinkUnit.GLASS)),
    ;

    public final List<DrinkUnit> allowedUnits;

    public boolean isUnitAllowed(DrinkUnit drinkUnit) {
        return allowedUnits.contains(drinkUnit);
    }
}
