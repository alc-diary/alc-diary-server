package com.example.alcdiary.presentation.dto.response;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindCalenderResponse {
    private Long calenderId;

    private String title;

    private String[] friends;

    private String drinkTime;

    private List<DrinksModel> drinks;
    private Integer totalDrinkCount;
    private String hangOver;
    private String contents;

    private String[] imageUrl;
}
