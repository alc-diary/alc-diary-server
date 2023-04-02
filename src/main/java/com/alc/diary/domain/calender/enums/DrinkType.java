package com.alc.diary.domain.calender.enums;

public enum DrinkType {
    SOJU("소주", 70, 800),
    BEER("맥주", 185, 1000),
    WINE("와인", 85, 4000),
    TRADITIONAL("막걸리", 92, 1000);


    private String name;
    private int calorie;
    private int price;

    DrinkType(String name, int calorie, int price) {
        this.name = name;
        this.calorie = calorie;
        this.price = price;
    }
}
