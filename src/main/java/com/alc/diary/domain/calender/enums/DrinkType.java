package com.alc.diary.domain.calender.enums;

import lombok.Getter;

@Getter
public enum DrinkType {
    SOJU("소주", 408, 4000),
    BEER("맥주", 256, 4000),
    WINE("와인", 548, 30000),
    TRADITIONAL("막걸리", 344, 4000);


    private String name;
    private int calorie;
    private int price;

    DrinkType(String name, int calorie, int price) {
        this.name = name;
        this.calorie = calorie;
        this.price = price;
    }
}
