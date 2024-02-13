package com.alc.diary.application.admin.drink;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.enums.DrinkType;

import java.time.LocalDateTime;

public record DrinkDto(

        long id,
        long categoryId,
        String name,
        Long creatorId,
        boolean isPublic,
        DrinkType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DrinkDto from(Drink drink) {
        return new DrinkDto(
                drink.getId(),
                drink.getCategoryId(),
                drink.getName(),
                drink.getCreatorId(),
                drink.getIsPublic(),
                drink.getType(),
                drink.getCreatedAt(),
                drink.getUpdatedAt());
    }
}
