package com.alc.diary.application.calendar.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record CreateCalendarRequest(

        @NotNull String title,
        @Size(max = 1000) String content,
        @NotNull LocalDateTime drinkStartTime,
        @NotNull LocalDateTime drinkEndTime,
        String drinkCondition,
        @NotNull List<UserCalendarDrinkDto> userCalendarDrinks,
        @NotNull List<UserCalendarImageDto> userCalendarImages,
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
