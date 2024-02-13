package com.alc.diary.application.admin.drink.request;

public record AdminCreateDrinkRequest(

        long drinkCategoryId,
        String name
) {
}
