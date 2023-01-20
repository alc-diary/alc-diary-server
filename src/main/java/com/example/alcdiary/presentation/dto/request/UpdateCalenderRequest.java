package com.example.alcdiary.presentation.dto.request;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCalenderRequest {

    @NotNull
    private String title;

    private String[] friends;

    @NotNull
    private List<DrinksModel> drinks;

    private String hangOver;

    private Time drinkStartTime;

    private Time drinkEndTime;

    private String imageUrl;

    private String contents;
}
