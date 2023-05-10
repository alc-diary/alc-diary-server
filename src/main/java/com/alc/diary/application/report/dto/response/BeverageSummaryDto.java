package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.BeverageSummary;
import com.alc.diary.domain.calender.enums.DrinkType;

public record BeverageSummaryDto(

        DrinkType drinkType,
        float totalAmount
) {

    public BeverageSummaryDto(BeverageSummary beverageSummary) {
        this(beverageSummary.getDrinkType(), beverageSummary.getTotalAmount());
    }
}
