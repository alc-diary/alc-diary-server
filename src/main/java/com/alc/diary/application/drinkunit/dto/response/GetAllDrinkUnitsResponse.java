package com.alc.diary.application.drinkunit.dto.response;

import com.alc.diary.domain.drinkunit.DrinkUnit;

public record GetAllDrinkUnitsResponse(

        long drinkUnitId,
        String drinkUnitName
) {

    public static GetAllDrinkUnitsResponse from(DrinkUnit drinkUnit) {
        return new GetAllDrinkUnitsResponse(drinkUnit.getId(), drinkUnit.getName());
    }
}
