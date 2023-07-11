package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.enums.DrinkType;

public record GetMonthlyCalendarsResponse(

        String date,
        DrinkType drinkType
) {
}
