package com.alc.diary.application.admin.response;

import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;

import java.time.LocalDateTime;

public record DrinkRecordDto(

        long id,
        DrinkType type,
        DrinkUnit unit,
        float quantity,
        LocalDateTime deletedAt
) {

    public static DrinkRecordDto fromDomainModel(DrinkRecord drinkRecord) {
        return new DrinkRecordDto(
                drinkRecord.getId(),
                drinkRecord.getType(),
                drinkRecord.getUnit(),
                drinkRecord.getQuantity(),
                drinkRecord.getDeletedAt()
        );
    }
}
