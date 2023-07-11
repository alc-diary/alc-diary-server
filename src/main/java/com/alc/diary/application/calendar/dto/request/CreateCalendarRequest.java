package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public record CreateCalendarRequest(

        @NotNull String title,
        @Size(max = 1000) String content,
        @NotNull ZonedDateTime drinkStartTime,
        @NotNull ZonedDateTime drinkEndTime,
        String drinkCondition,
        @NotNull List<DrinkCreationDto> drinks,
        @NotNull List<PhotoCreationDto> photos,
        @NotNull Set<Long> taggedUserIds
) {

    public record DrinkCreationDto(

            DrinkType drinkType,
            DrinkUnit drinkUnit,
            float quantity
    ) {
    }

    public record PhotoCreationDto(

            String url
    ) {
    }
}
