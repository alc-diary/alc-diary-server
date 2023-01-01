package com.example.alcdiary.domain.model.calender;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DrinkReportModel {
    private final Long price;
    private final Long calorie;

    @Builder
    public DrinkReportModel(Long price, Long calorie) {
        this.price = price == null ? 0L : price;
        this.calorie = calorie == null ? 0L: calorie;
    }
}
