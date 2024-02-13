package com.alc.diary.application.calendar.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record CreateCalendarRequestV2(

        @NotNull @Size(max = 100) String title,
        @NotNull String drinkDate,
        @Valid @NotNull @Size(max = 20) List<PhotoCreateDto> photos,
        @Valid @NotNull UserCalendarCreationDto userCalendar,
        @NotNull List<Long> taggedUserIds
        ) {

    public record UserCalendarCreationDto(

            Long userId,
            String content,
            String condition,
            List<DrinkCreationDto> drinks
    ) {
    }

    public record DrinkCreationDto(

            @NotNull Long drinkId,
            @NotNull Long drinkUnitId,
            @NotNull @Min(0) Float quantity
    ) {
    }

    public record PhotoCreateDto(

            @NotNull @Size(max = 1000) String url
    ) {
    }
}
