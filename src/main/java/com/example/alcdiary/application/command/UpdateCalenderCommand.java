package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCalenderCommand {
    private String userId;

    private String title;

    private String[] friends;

    private List<DrinksModel> drinks;

    private String hangOver;

    private Time drinkStartTime;

    private Time drinkEndTime;

    private String imageUrl;

    private String contents;
}
