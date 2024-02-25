package com.alc.diary.application.drink.dto.request;

import javax.validation.constraints.Size;

public record CreateDrinkRequest(

        long drinkCategoryId,
        @Size(max = 30) String drinkName
) {
}
