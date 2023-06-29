package com.alc.diary.application.drinkcategory.dto.response;

import com.alc.diary.domain.drinkcategory.DrinkCategory;

public record GetAllDrinkCategoriesResponse(

        long drinkCategoryId,
        String drinkCategoryName
) {

    public static GetAllDrinkCategoriesResponse from(DrinkCategory drinkCategory) {
        return new GetAllDrinkCategoriesResponse(drinkCategory.getId(), drinkCategory.getName());
    }
}
