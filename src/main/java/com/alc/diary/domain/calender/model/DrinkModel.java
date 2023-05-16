package com.alc.diary.domain.calender.model;


import com.alc.diary.domain.calender.enums.DrinkType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkModel {
    private DrinkType type;
    private float quantity;

    @JsonIgnore
    public int getTotalPrice() {
        return Math.round(quantity * type.getPrice());
    }

    @JsonIgnore
    public int getTotalCalorie() {
        return Math.round(quantity * type.getCalorie());
    }
}