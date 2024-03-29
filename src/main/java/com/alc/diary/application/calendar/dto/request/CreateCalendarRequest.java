package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

public record CreateCalendarRequest(

        @NotNull @Size(max = 100) String title,
        @NotNull ZonedDateTime drinkStartTime,
        @NotNull ZonedDateTime drinkEndTime,
        @Valid @NotNull @Size(max = 20) List<PhotoCreationDto> photos,
        @Valid @NotNull UserCalendarCreationDto userCalendar,
        @NotNull List<Long> taggedUserIds
) {

    @ApiModel(value = "CreateCalendarRequest_UserCalendarCreationDto")
    public record UserCalendarCreationDto(

            @NotNull Long userId,
            @Size(max = 1000) String content,
            String condition,
            @Valid @NotNull List<DrinkCreationDto> drinks
    ) {
    }

    @ApiModel(value = "CreateCalendarRequest_DrinkCreationDto")
    public record DrinkCreationDto(

            @NotNull DrinkType drinkType,
            @NotNull DrinkUnitType drinkUnit,
            @NotNull @Min(0) Float quantity
    ) {
    }

    @ApiModel(value = "CreateCalendarRequest_PhotoCreationDto")
    public record PhotoCreationDto(

            @NotNull @Size(max = 1000) String url
    ) {
    }
}
