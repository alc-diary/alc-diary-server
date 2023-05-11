package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import lombok.Getter;
import lombok.ToString;

public record BeverageSummary(DrinkType drinkType, float totalAmount) {
}
