package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.model.user.EUserAlcoholType;
import com.example.alcdiary.domain.model.user.EUserTheme;
import lombok.Getter;

@Getter
public class SaveUserInfoCommand {

    private EUserTheme theme;
    private String username;
    private EUserAlcoholType alcoholType;
    private int drinkCapacity;
    private int resolutionDays;
}
