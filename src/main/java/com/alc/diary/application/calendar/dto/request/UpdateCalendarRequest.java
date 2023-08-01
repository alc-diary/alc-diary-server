package com.alc.diary.application.calendar.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

public record UpdateCalendarRequest(

        @NotNull @Size(max = 100) String title,
        @NotNull ZonedDateTime drinkStartTime,
        @NotNull ZonedDateTime drinkEndTime,
        @NotNull List<Long> taggedUsers
) {
}
