package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public record CreateCalendarFromMainRequest(

        ZonedDateTime drinkStartTime,
        ZonedDateTime drinkEndTime,
        @Valid @NotNull List<DrinkCreationDto> drinks
) {

    @ApiModel(value = "CreateCalendarFromMainRequest_DrinkCreationDto")
    public record DrinkCreationDto(

            @NotNull DrinkType drinkType,
            @NotNull DrinkUnitType drinkUnit,
            @NotNull @Min(0) Float quantity
    ) {
    }
}
