package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.presentation.dto.response.FindCalenderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindCalenderResult {

    private Long calenderId;

    private String title;
    private String hangOver;

    private DrinksModel[] drinks;

    public FindCalenderResult fromModel(CalenderModel calenderModel) {
        return FindCalenderResult.builder()
                .calenderId(calenderModel.getId())
                .title(calenderModel.getTitle())
                .hangOver(calenderModel.getHangOver())
                .drinks(calenderModel.getDrinks())
                .build();
    }

    public FindCalenderResponse toResponse() {
        return FindCalenderResponse.builder()
                .calenderId(calenderId)
                .title(title)
                .hangOver(hangOver)
                .drinks(drinks)
                .build();
    }
}
