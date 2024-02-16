package com.alc.diary.application.drinkunit;

import com.alc.diary.domain.drinkunit.DrinkUnit;

public record DrinkUnitDto(

        long id,
        String name
) {

    public static DrinkUnitDto fromDomainModel(DrinkUnit drinkUnit) {
        return new DrinkUnitDto(drinkUnit.getId(), drinkUnit.getName());
    }
}
