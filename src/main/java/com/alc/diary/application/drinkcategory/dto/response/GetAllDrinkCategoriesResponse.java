package com.alc.diary.application.drinkcategory.dto.response;

import com.alc.diary.domain.drinkcategory.DrinkCategory;

public record GetAllDrinkCategoriesResponse(

        long id,
        String name,
        String imageUrl,
        long defaultDrinkId
) {

    public static GetAllDrinkCategoriesResponse from(DrinkCategory drinkCategory) {
        return new GetAllDrinkCategoriesResponse(
                drinkCategory.getId(),
                drinkCategory.getName(),
                drinkCategory.getImageUrl(),
                drinkCategory.getDefaultDrinkBrandId());
    }
}
