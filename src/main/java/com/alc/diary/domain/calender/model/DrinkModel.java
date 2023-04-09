package com.alc.diary.domain.calender.model;


import com.alc.diary.domain.calender.enums.DrinkType;

public class DrinkModel {
    private DrinkType type;
    private Integer quantity;

    public DrinkModel(DrinkType drinkType, int quantity) {
        this.type = drinkType;
        this.quantity = quantity;
    }
}