package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.enums.DrinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCalenderCommand {
    private String title;
    private String contents;
    private DrinkType drinkType;
    private String hangOver;
}
