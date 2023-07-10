package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.drink.DrinkType;
import com.alc.diary.domain.drink.DrinkUnit;

import java.time.ZonedDateTime;
import java.util.List;

public record UpdateCalendarRequest(

        String title,
        String content,
        boolean contentShouldBeUpdated,
        String condition,
        boolean conditionShouldBeUpdated,
        ZonedDateTime drinkStartTime,
        ZonedDateTime drinkEndTime,
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
