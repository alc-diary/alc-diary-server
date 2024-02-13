package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;

import java.util.List;

public record UpdateUserCalendarRequest(

        String content,
        boolean contentShouldBeUpdated,
        String condition,
        boolean conditionShouldBeUpdated,
        UpdateDrinkRecordData drinks,
        UpdatePhotoData photos
) {

    public record UpdateDrinkRecordData(

            List<DrinkRecordCreationData> added,
            List<DrinkRecordUpdateData> updated,
            List<Long> deleted
    ) {
    }

    public record DrinkRecordCreationData(

            DrinkType drinkType,
            DrinkUnitType drinkUnitType,
            float quantity
    ) {
    }

    public record DrinkRecordUpdateData(

            long id,
            DrinkType drinkType,
            DrinkUnitType drinkUnitType,
            float quantity
    ) {
    }

    public record UpdatePhotoData(

            List<ImageCreationData> added,
            List<Long> deleted
    ) {
    }

    public record ImageCreationData(

            String url
    ) {
    }
}
