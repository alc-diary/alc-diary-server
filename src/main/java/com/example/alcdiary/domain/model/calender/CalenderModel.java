package com.example.alcdiary.domain.model.calender;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CalenderModel {

    private Long id;
    private String title;
    private String hangOver;

    private DrinksModel[] drinks;
}
