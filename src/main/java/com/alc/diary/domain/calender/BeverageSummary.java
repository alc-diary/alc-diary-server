package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public class BeverageSummary {

    private final DrinkType drinkType;
    private final float totalAmount;

    public BeverageSummary(DrinkType drinkType, float totalAmount) {
        this.drinkType = drinkType;
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeverageSummary that = (BeverageSummary) o;
        return Float.compare(that.totalAmount, totalAmount) == 0 && drinkType == that.drinkType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(drinkType, totalAmount);
    }
}
