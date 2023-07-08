package com.alc.diary.application.calendar.dto.request;

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
        UpdateDrinkData drinks,
        UpdateImageData images
) {

    public record UpdateDrinkData(

            List<DrinkCreationData> added,
            List<DrinkUpdateData> updated,
            List<Long> deleted
    ) {
    }

    public record DrinkCreationData(

            long drinkUnitInfoId,
            float quantity
    ) {
    }

    public record DrinkUpdateData(

            long id,
            long drinkUnitInfoId,
            float quantity
    ) {
    }

    public record UpdateImageData(

            List<ImageCreationData> added,
            List<Long> deleted
    ) {
    }

    public record ImageCreationData(

            String imageUrl
    ) {
    }
}
