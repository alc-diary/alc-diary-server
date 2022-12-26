package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCalenderCommand {
    private String title;

    private String[] friends;

    private DrinksModel[] drinks;

    private String hangOver;

    private Time drinkStartTime;

    private Time drinkEndTime;

    private String[] imageUrl;

    private String contents;
}
