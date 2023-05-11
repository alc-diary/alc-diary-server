package com.alc.diary.application.report.dto.response;

import com.alc.diary.domain.calender.BeverageSummary;
import com.alc.diary.domain.calender.enums.DrinkType;

public record BeverageSummaryDto(

        DrinkType drinkType,
        float totalAmount,
        float amountChangeFromLastMonth
) {

    public BeverageSummaryDto(BeverageSummary currentMonthBeverageSummary, BeverageSummary lastMonthBeverageSummary) {
        this(
                currentMonthBeverageSummary.drinkType(),
                currentMonthBeverageSummary.totalAmount(),
                currentMonthBeverageSummary.totalAmount() - lastMonthBeverageSummary.totalAmount()
        );
    }
}
