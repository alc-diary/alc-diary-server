package com.example.alcdiary.domain.model.calender;

import com.example.alcdiary.domain.enums.DrinkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DrinksModel {
    private DrinkType type;
    private Integer quantity;

}
