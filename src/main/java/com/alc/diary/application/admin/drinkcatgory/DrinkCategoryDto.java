package com.alc.diary.application.admin.drinkcatgory;

import com.alc.diary.domain.drinkcategory.DrinkCategory;

import java.time.LocalDateTime;

public record DrinkCategoryDto(

        long id,
        String name,
        String imageUrl,
        long defaultDrinkBrandId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DrinkCategoryDto from(DrinkCategory drinkCategory) {
        return new DrinkCategoryDto(
                drinkCategory.getId(),
                drinkCategory.getName(),
                drinkCategory.getImageUrl(),
                drinkCategory.getDefaultDrinkBrandId(),
                drinkCategory.getCreatedAt(),
                drinkCategory.getUpdatedAt()
        );
    }
}
