package com.alc.diary.application.calendar.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record UpdateCalendarRequestV2(

        String title,
        LocalDate drinkDate,

        List<UserCalendarDto> userCalendars,
        List<Long> deleteUserCalendars,

        List<PhotoDto> photos,
        List<Long> deletePhotos
) {

    public record UserCalendarDto(
            Long id,
            Optional<String> content,
            Optional<String> drinkCondition,
            Boolean drinkingRecorded,

            List<DrinkRecordDto> drinkRecords,
            List<Long> deleteDrinkRecords
    ) {

        public record DrinkRecordDto(
                Long id,
                Long drinkId,
                Long drinkUnitId,
                Float quantity
        ) {
        }
    }

    public record PhotoDto(
            Long id,
            String url
    ) {
    }
}
