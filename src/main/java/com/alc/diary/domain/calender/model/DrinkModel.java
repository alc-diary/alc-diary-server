package com.alc.diary.domain.calender.model;


import com.alc.diary.domain.calender.enums.DrinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkModel {
    private DrinkType type;
    private float quantity;

    public float getQuantity() {
        return quantity;
    }
}