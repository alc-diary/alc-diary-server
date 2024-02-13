package com.alc.diary.application.admin.drink;

import com.alc.diary.domain.drink.Drink;

import java.time.LocalDateTime;

public record DrinkDto(

        long id,
        long categoryId,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DrinkDto from(Drink drink) {
        return new DrinkDto(drink.getId(), drink.getCategoryId(), drink.getName(), drink.getCreatedAt(), drink.getUpdatedAt());
    }
}
