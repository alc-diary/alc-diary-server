package com.alc.diary.application.drink.dto.response;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkunit.DrinkUnit;

import java.util.List;
import java.util.Map;

public record GetAllDrinksResponse(

        String categoryName,
        List<DrinkDto> drinks
) {

    public static List<GetAllDrinksResponse> of(
            List<DrinkCategory> drinkCategories,
            Map<Long, List<Drink>> drinkUnitInfoByCategoryId,
            Map<Long, DrinkUnit> drinkUnitById
    ) {
        return drinkCategories.stream()
                .map(drinkCategory -> {
                    List<Drink> drinks = drinkUnitInfoByCategoryId.get(drinkCategory.getId());
                    List<DrinkDto> drinkDtos = drinks.stream()
                            .map(drink -> {
                                // List<DrinkUnitInfoDto> drinkUnitInfoDtos = drink.getDrinkUnitInfos().stream()
                                //         .map(drinkUnitInfo -> new DrinkUnitInfoDto(
                                //                 drinkUnitInfo.getId(),
                                //                 drinkUnitById.get(drinkUnitInfo.getDrinkUnitId()).getName()
                                //         ))
                                //         .toList(); // FIXME
                                return new DrinkDto(
                                        drink.getName(),
                                        // drinkUnitInfoDtos
                                        List.of()
                                );
                            })
                            .toList();
                    return new GetAllDrinksResponse(
                            drinkCategory.getName(),
                            drinkDtos
                    );
                })
                .toList();
    }

    public record DrinkDto(

            String name,
            List<DrinkUnitInfoDto> drinkUnitInfos
    ) {
    }

    public record DrinkUnitInfoDto(

            long id,
            String unitName
    ) {
    }
}
