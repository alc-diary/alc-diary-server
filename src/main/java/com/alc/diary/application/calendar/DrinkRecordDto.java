package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.DrinkRecord;

public record DrinkRecordDto(

        long id,
        long drinkId,
        long drinkUnitId,
        float quantity
) {

    public static DrinkRecordDto fromDomainModel(DrinkRecord drinkRecord) {
        return new DrinkRecordDto(
                drinkRecord.getId(),
                drinkRecord.getDrinkId(),
                drinkRecord.getDrinkUnitId(),
                drinkRecord.getQuantity()
        );
    }
}
