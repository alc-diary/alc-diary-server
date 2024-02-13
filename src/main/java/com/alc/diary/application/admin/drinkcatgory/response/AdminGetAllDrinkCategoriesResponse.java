package com.alc.diary.application.admin.drinkcatgory.response;

import com.alc.diary.domain.drinkcategory.DrinkCategory;

public record AdminGetAllDrinkCategoriesResponse(

        long id,
        String name
) {

    public static AdminGetAllDrinkCategoriesResponse from(DrinkCategory drinkCategory) {
        return new AdminGetAllDrinkCategoriesResponse(drinkCategory.getId(), drinkCategory.getName());
    }
}
