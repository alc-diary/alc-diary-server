package com.alc.diary.application.calendar.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public record CreateCalendarRequest(

        @NotNull String title,
        @Size(max = 1000) String content,
        @NotNull ZonedDateTime drinkStartTime,
        @NotNull ZonedDateTime drinkEndTime,
        String drinkCondition,
        @NotNull List<UserCalendarDrinkDto> userCalendarDrinks,
        @NotNull @Size(max = 5, message = "The number of images cannot exceed 5.") List<UserCalendarImageDto> userCalendarImages,
        @NotNull Set<Long> taggedUserIds
) {

    public record UserCalendarDrinkDto(

            long drinkUnitInfoId,
            float quantity
    ) {
    }

    public record UserCalendarImageDto(

            String imageUrl
    ) {
    }
}
