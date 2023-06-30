package com.alc.diary.application.drink.dto.request;

public record AddDrinkUnitInfoRequest(

        long drinkUnitId,
        int price,
        int calories
) {
}
