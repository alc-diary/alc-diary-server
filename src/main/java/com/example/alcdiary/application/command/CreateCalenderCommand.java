package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.model.calender.DrinkReportModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Time;
import java.util.List;

@Getter
@Builder
@Data
public class CreateCalenderCommand {
    private String userId;

    private String title;

    private String [] friends;

    private List<DrinksModel> drinks;

    private String hangOver;

    private Time drinkStartTime;

    private Time drinkEndTime;

    private String imageUrl;

    private String contents;

    private DrinkReportModel drinkReport;
}
