package com.alc.diary.application.drinkcategory;

import com.alc.diary.application.drinkunit.DrinkUnitDto;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.drinkcategory.DrinkCategory;

import java.util.List;

public record DrinkCategoryDto(

        long id,
        String name,
        String imageUrl,
        long defaultDrinkId
) {

    public static DrinkCategoryDto fromDomainModel(DrinkCategory drinkCategory) {
        return new DrinkCategoryDto(
                drinkCategory.getId(),
                drinkCategory.getName(),
                drinkCategory.getImageUrl(),
                drinkCategory.getDefaultDrinkBrandId());
    }
}
