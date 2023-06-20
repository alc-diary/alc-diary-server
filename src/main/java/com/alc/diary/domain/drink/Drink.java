package com.alc.diary.domain.drink;

import lombok.Getter;

@Getter
public enum Drink {

    BEER(408, 4_000),
    SOJU(256, 4_000),
    WINE(548, 30_000),
    MAKGEOLLI(344, 4_000),
    ;

    Drink(int calorie, int price) {
    }
}
