package com.alc.diary.application.admin.drinkunit;

import com.alc.diary.domain.drinkunit.DrinkUnit;

import java.time.LocalDateTime;

public record DrinkUnitDto(

        long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DrinkUnitDto from(DrinkUnit drinkUnit) {
        return new DrinkUnitDto(
                drinkUnit.getId(),
                drinkUnit.getName(),
                drinkUnit.getCreatedAt(),
                drinkUnit.getUpdatedAt()
        );
    }
}
