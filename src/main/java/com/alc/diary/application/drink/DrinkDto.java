package com.alc.diary.application.drink;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.enums.DrinkType;

public record DrinkDto(

        long id,
        long categoryId,
        String name,
        DrinkType type) {

    public static DrinkDto fromDomainModel(Drink drink) {
        return new DrinkDto(
                drink.getId(),
                drink.getCategoryId(),
                drink.getName(),
                drink.getType()
        );
    }
}
