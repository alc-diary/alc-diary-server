package com.example.alcdiary.domain.model.calender;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class CalenderModel {

    private Long id;
    private String title;

    private String[] friends;

    private Time drinkStartTime;
    private Time drinkEndTime;

    private LocalDateTime createdAt;

    private List<DrinksModel> drinks;
    private String hangOver;
    private String contents;

    private String[] imageUrl;
}
