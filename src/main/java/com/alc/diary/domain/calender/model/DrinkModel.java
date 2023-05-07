package com.alc.diary.domain.calender.model;


import com.alc.diary.domain.calender.enums.DrinkType;
import lombok.*;

@Builder
@ToString
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