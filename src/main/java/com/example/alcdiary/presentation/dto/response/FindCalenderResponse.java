package com.example.alcdiary.presentation.dto.response;

import com.example.alcdiary.domain.model.calender.DrinksModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindCalenderResponse {
    private Long calenderId;

    private String title;

    private String drinkTime;
    private String[] friends;

    private DrinksModel[] drinks;
    private Integer totalDrinkCount;
    private String hangOver;
    private String contents;

    private String[] imageUrl;
}
