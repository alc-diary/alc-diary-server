package com.example.alcdiary.domain.enums;

import com.example.alcdiary.domain.model.calender.DrinkReportModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;

import java.util.List;

public enum DrinkType {
    SOJU(800, 70),
    BEER(1000, 70),
    WINE(4000, 70),
    TRADITIONAL(1000, 70);

    public final int price;
    public final int calorie;

    DrinkType(int price, int calorie) {
        this.price = price;
        this.calorie = calorie;
    }

    // TODO: 마신 잔 수 세는 계산기 필요
    public static Integer calculateTotalDrinks(List<DrinksModel> drinks) {
        return drinks.stream()
                .mapToInt(DrinksModel::getQuantity).sum();
    }

    public static DrinkReportModel calculate(List<DrinksModel> drinksModel) {
        int prices = 0;
        int calories = 0;
        //  가격 * 수량
        //  칼로리 * 수량

        return DrinkReportModel.builder()
                .price((long) prices)
                .calorie((long) calories)
                .build();
    }
}
