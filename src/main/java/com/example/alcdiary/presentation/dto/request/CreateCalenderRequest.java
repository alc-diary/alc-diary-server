package com.example.alcdiary.presentation.dto.request;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalenderRequest {
    @NotNull
    private String title;

    private String[] friends;

    @NotNull
    private DrinksModel[] drinks;
    private String hangOver;

    @NotNull
    private Time drinkStartTime;

    @NotNull
    private Time drinkEndTime;

    // TODO: multipart로 개선
    private String[] imageUrl;

    @NotNull
    private String contents;
}
