package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.DrinkSummary;
import com.alc.diary.domain.calender.enums.DrinkType;

public record DrinkSummaryDto(

        DrinkType drinkType,
        float quantity
) {

    public DrinkSummaryDto(DrinkSummary drinkSummary) {
        this(
                drinkSummary.drinkType(),
                drinkSummary.totalQuantity()
        );
    }
}
