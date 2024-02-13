package com.alc.diary.application.admin.drink.request;

public record AdminCreateDrinkRequest(

        String name,
        long drinkCategoryId
) {
}
