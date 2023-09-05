package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;

import java.util.List;

public record UpdateUserCalendarRequest(

        String content,
        boolean contentShouldBeUpdated,
        String condition,
        boolean conditionShouldBeUpdated,
        UpdateDrinkRecordData drinks
) {

    public record UpdateDrinkRecordData(

            List<DrinkRecordCreationData> added,
            List<DrinkRecordUpdateData> updated,
            List<Long> deleted
    ) {
    }

    public record DrinkRecordCreationData(

            DrinkType drinkType,
            DrinkUnit drinkUnit,
            float quantity
    ) {
    }

    public record DrinkRecordUpdateData(

            long id,
            DrinkType drinkType,
            DrinkUnit drinkUnit,
            float quantity
    ) {
    }
}
