package com.example.alcdiary.domain.model.calender;

import com.example.alcdiary.domain.enums.DrinkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrinksModel {
    private DrinkType type;
    private Integer quantity;
}
