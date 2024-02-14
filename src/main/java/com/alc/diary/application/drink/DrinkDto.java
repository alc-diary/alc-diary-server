package com.alc.diary.application.drink;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.enums.DrinkType;

public record DrinkDto(

        long id,
        long categoryId,
        String name,
        Boolean isPublic,
        DrinkType type) {

    public static DrinkDto from(Drink drink) {
        return new DrinkDto(
                drink.getId(),
                drink.getCategoryId(),
                drink.getName(),
                drink.getIsPublic(),
                drink.getType()
        );
    }
}
