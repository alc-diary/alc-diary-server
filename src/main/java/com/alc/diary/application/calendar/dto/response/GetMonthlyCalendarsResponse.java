package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.drink.DrinkType;
import com.alc.diary.domain.drink.DrinkUnit;
import io.swagger.annotations.ApiModel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record GetMonthlyCalendarsResponse(

        String date,
        DrinkType drinkType
) {
}
