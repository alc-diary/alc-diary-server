package com.alc.diary.application.drink.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record CreateDrinkRequest(

        long drinkCategoryId,
        @Size(max = 30) String drinkName,
        @NotNull List<DrinkUnitDto> drinkUnits
) {

    public record DrinkUnitDto(

            long drinkUnitId,
            int price,
            int calories
    ) {
    }
}
