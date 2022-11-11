package com.example.alcdiary.presentation.dto.request;

import com.example.alcdiary.domain.enums.DrinkType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalenderRequest {
    @NotNull
    private String title;
    @NotNull
    private String contents;
    @NotNull
    private DrinkType drinkType;
    private String hangOver;
}
