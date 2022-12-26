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
    private String hangOver;

    private DrinksModel[] drinks;
}
